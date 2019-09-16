package com.cb.pro.tme.emu;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static final LinkedBlockingQueue<String> TME_EVENT_QUEUE = new LinkedBlockingQueue<>();

	public static final List<Order> ORDERS = new CopyOnWriteArrayList<Order>();

	public static double CURRENT_PRICE = 9000.0;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
