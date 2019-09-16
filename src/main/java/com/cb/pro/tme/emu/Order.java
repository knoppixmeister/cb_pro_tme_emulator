package com.cb.pro.tme.emu;

import java.util.UUID;

public class Order {
	public final String id;

	public Order() {
		this.id = UUID.randomUUID().toString();
	}
}
