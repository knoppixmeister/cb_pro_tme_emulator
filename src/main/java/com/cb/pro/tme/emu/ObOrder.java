package com.cb.pro.tme.emu;

import com.cb.pro.tme.utils.Utils;

/*
 * "id": "d0c5340b-6d6c-49d9-b567-48c4bfca13d2",
    "price": "0.10000000",
    "size": "0.01000000",
    "product_id": "BTC-USD",
    "side": "buy",
    "stp": "dc",
    "type": "limit",
    "time_in_force": "GTC",
    "post_only": false,
    "created_at": "2016-12-08T20:02:28.53864Z",
    "fill_fees": "0.0000000000000000",
    "filled_size": "0.00000000",
    "executed_value": "0.0000000000000000",
    "status": "pending",
    "settled": false
*/

public class ObOrder {
	public final String id;

	private final transient String client_oid;

	public String price;
	public String size;
	public String product_id;
	public String side;
	public String stp;
	public String type;
	public String time_in_force;
	public boolean post_only;
	public String created_at;
	public String fill_fees = "0.0";
	public String filled_size = "0.0";
	public String executed_value = "0.0";
	public String status = "pending";
	public boolean settled = false;

	public ObOrder(RequestOrder baseOrder) {
		this.id = baseOrder.getId();

		this.client_oid = baseOrder.client_oid;

		this.price = baseOrder.price;
		this.size = baseOrder.size;
		this.product_id = baseOrder.product_id;
		this.side = baseOrder.side;
		this.stp = baseOrder.stp;
		this.type = baseOrder.type;
		this.time_in_force = baseOrder.time_in_force;
		this.created_at = Utils.isoDateTime();
	}

	public String getClientOid() {
		return this.client_oid;
	}
}
