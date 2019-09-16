package com.cb.pro.tme.emu;

import java.util.UUID;

public class RequestOrder {
	private final transient String id = UUID.randomUUID().toString();

	public String client_oid;
	public String type = "limit";
	public String side;
	public String product_id;//BTC-EUR
	public String stp = "dc";
	public String stop;
	public String stop_price;

	public String price;
	public String size;
	public String time_in_force = "GTC";
	public String post_only = "true";

	public String getId() {
		return this.id;
	}
}
