package com.cb.pro.tme.emu;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static String API_KEY = "";
	public static String API_SECRET = "";
	public static String API_PASSPHRASE = "";

	public static final LinkedBlockingQueue<String> TME_EVENTS_QUEUE = new LinkedBlockingQueue<>();

	public static final List<ObOrder> ORDERS = new CopyOnWriteArrayList<>();

	public static double CURRENT_PRICE = 9000.0;

	private static final Scanner SCANNER = new Scanner(System.in);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		String cmd;
		try {
			while(true) {
				System.out.print(">_: ");
				cmd = SCANNER.nextLine();

				if(cmd != null && !cmd.equals("")) {
					cmd = cmd.trim();

					switch(cmd) {
						case "cp":	System.out.println("GET CURRENT PRICE --> "+CURRENT_PRICE);
									break;
						case "scp":	System.out.println("SET CURRENT PRICE");
									setCurrentPrice();
									break;
						case "l":
						case "lo":	System.out.println("LIST ORDERS");
									listOrders();
									break;
						case "do":	System.out.println("DELETE ORDER");
									deleteOrder();
									break;
						case "co":	System.out.println("CANCEL ORDER");
									cancelOrder();
									break;
						case "clo":	System.out.println("CREATE LIMIT ORDER");
									//createOrder(Order.OrderType.LIMIT);
									break;
						case "cmo":	System.out.println("CREATE MARKET ORDER");
									//createOrder(Order.OrderType.MARKET);
									break;
						case "m":
						case "mm":	System.out.println("MAKE ORDER MATCH");
									makeMatch();
									break;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
		{
			"type":				"match"
			"trade_id":			21019893
			"maker_order_id":	"8a0efaab-e721-4ac2-bd0e-daa5b80f4cc5"
			"taker_order_id":	"2e7ac867-6b29-4536-8f1b-013ccd978b5e"
			"side":				"buy"
			"size":				"0.00111458"
			"price":			"8972.00000000"
			"product_id":		"BTC-EUR"
			"sequence":			5676008679
			"time":				"2019-07-23T17:55:36.686000Z"
		}
	
		{
			"type":				"done"
			"side":				"buy"
			"order_id":			"8a0efaab-e721-4ac2-bd0e-daa5b80f4cc5"
			"reason":			"filled"
			"product_id":		"BTC-EUR"
			"price":			"8972.00000000"
			"remaining_size":	"0"
			"sequence":			5676008680
			"time":				"2019-07-23T17:55:36.686000Z"
		}
	*/

	private static void makeMatch() {
		String cmd;

		System.out.print("ENTER ORDER ID: ");
		cmd = SCANNER.nextLine();

		try {
			UUID.fromString(cmd);
	
			for(ObOrder o : ORDERS) {
				if(o.id.equals(cmd)) {
					System.out.println("\r\nFOUND ORDER: "+o.id+" | SD: "+o.side+" | SZ: "+o.size+" | PR: "+o.price+"\r\n");

					System.out.print("ENTER MATCH AMOUNT: ");
					cmd = SCANNER.nextLine();
	
					double matchAmount;
					try {
						matchAmount = Double.parseDouble(cmd);
	
						String wsResponse;
						if(matchAmount >= Double.parseDouble(o.size)) {
							wsResponse = 	"{"+
											"\"type\":				\"done\","+
											"\"side\":				\"buy\","+
											"\"order_id\":			\""+o.id+"\"," + 
											"\"reason\":			\"filled\"," + 
											"\"product_id\":		\""+o.product_id+"\"," + 
											"\"price\":				\""+o.price+"\"," +
											"\"remaining_size\":	\"0\"," + 
											"\"sequence\":			5676008680,"+
											"\"time\":				\"2019-07-23T17:55:36.686000Z\""+ 
											"}";
	
							if(Application.ORDERS.remove(o)) {
								Application.TME_EVENTS_QUEUE.add(wsResponse);

								System.out.println("ORDER FULLY MATCHED. REMOVED FROM LIST");								
							}
	
							return;
						}
						else {
							wsResponse = 	"{" +
											"\"type\":				\"match\","+
											"\"trade_id\":			21019893,"+
											"\"maker_order_id\":	\""+o.id+"\","+
											"\"taker_order_id\":	\"2e7ac867-6b29-4536-8f1b-013ccd978b5e\","+
											"\"side\":				\"buy\","+
											"\"size\":				\""+matchAmount+"\","+
											"\"price\":				\""+o.price+"\"," + 
											"\"product_id\":		\""+o.product_id+"\"," + 
											"\"sequence\":			5676008679," + 
											"\"time\":				\"2019-07-23T17:55:36.686000Z\""+ 
											"}";

							Application.TME_EVENTS_QUEUE.add(wsResponse);

							System.out.println("PARTIALLY MATCHED.");
							return;
						}
					}
					catch(Exception e) {
						System.out.println("INVALID SIZE");
	
						return;
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println("INVALID ORDER ID.");
		}
	
		System.out.println("ORDER NOT FOUND. EXIT PROC");
	}

	private static void fullMatch() {
		;
	}
	
	private static void createOrder() {// Order.OrderType orderType
		Order order = new Order();
	
		// Application.ORDERS.add(order);
	
		String wsResponse = "{\"type\":\"received\","+
							"\"order_id\":\""+order.id+"\","+
							"\"order_type\":\"limit\","+
							"\"size\":\"0.00125000\","+
							"\"price\":\"8000.00000000\","+
							"\"side\":\"buy\","+
							//"\"client_oid\":\"8219a3c4-cbeb-4e7a-b682-85e64e86c583\", " + 
							"\"product_id\":\"BTC-EUR\","+
							"\"sequence\":5675665360,"+
							"\"time\":\"2019-07-23T16:37:52.109000Z\"}";

		Application.TME_EVENTS_QUEUE.add(wsResponse);		
	}

	private static void listOrders() {
		if(ORDERS == null || ORDERS.isEmpty()) System.out.println("<NO ORDERS>");
		else {
			int idx = 1;
			for(ObOrder o : ORDERS) {
				System.out.println(idx+"] "+o.id+" | SD: "+o.side+" | SZ: "+o.size+" | PR: "+o.price);

				++idx;
			}
		}
	}

	private static void deleteOrder() {
		String cmd;

		System.out.print("ENTER ID: ");
		cmd = SCANNER.nextLine();

		for(ObOrder o : ORDERS) {
			if(o.id.equals(cmd)) {
				if(Application.ORDERS.remove(o)) break;
			}
		}
	}
	
	private static void cancelOrder() {
		String cmd;
	
		System.out.print("ENTER ORDER ID: ");
		cmd = SCANNER.nextLine();
	
		for(ObOrder o : ORDERS) {
			if(o.id.equals(cmd)) {
				String wsResponse = "{\"type\":\"done\""+
									"\"side\":\"buy\""+
									"\"order_id\":\""+o.id+"\""+
									"\"reason\":\"canceled\""+
									"\"product_id\":\"BTC-EUR\""+
									"\"price\":\"8000.00000000\""+
									"\"remaining_size\":\"0.00125000\""+
									"\"sequence\":5675666889"+
									"\"time\":\"2019-07-23T16:38:08.845000Z\"}";
	
				if(Application.ORDERS.remove(o)) {
					// SpringTestApplication.EVENTS_QUEUE.add(wsResponse);
	
					break;
				}
			}
		}
	}

	private static void setCurrentPrice() {
		String cmd;
	
		System.out.print("ENTER PRICE: ");
		cmd = SCANNER.nextLine();
	
		if(cmd == null || cmd.isEmpty()) return;
	
		try {
			double newCp = Double.parseDouble(cmd);
	
			Application.CURRENT_PRICE = newCp;
	
			System.out.println("CURRENT PRICE CHANGED. NEW VALUE: "+CURRENT_PRICE);
		}
		catch(Exception e) {
			System.out.println("WRONG PRICE NUMBER. LEAVE OLD VALUE: "+CURRENT_PRICE);
		}
	}
}
