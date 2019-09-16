package com.cb.pro.tme.emu;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cb.pro.tme.utils.Utils;
import com.squareup.moshi.Moshi;

@RestController
public class ApiRestOrdersController {
	@GetMapping(value = "/orders")
	public List<Order> orders() {
		return Application.ORDERS;
	}

	@GetMapping(value = "/orders/{id}")
	public ResponseEntity<String> orders(@PathVariable(name = "id") String id) {
		if(id != null && !id.isEmpty()) {
			if(!Utils.isValidUUID(id)) {
				return new ResponseEntity<>("{\"message\":\"Invalid order id\"}", HttpStatus.BAD_REQUEST);
			}
		}

		for(Order o : Application.ORDERS) {
			if(o.id.equals(id)) {
				return new ResponseEntity<String>(
					new Moshi.Builder().build().adapter(Order.class).toJson(o),
					HttpStatus.OK
				);
			}
		}

		return new ResponseEntity<String>("{\"message\":\"NotFound\"}", HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/orders")
	public Order setOrder(@RequestBody String body) {
		;

		return null;
	}

	@DeleteMapping(value = "/orders")
	public String cancelOrders() {

		return "";
	}

	@DeleteMapping(value = "/orders/{id}")
	public String cancelOrders(@PathVariable(name = "id") String id) {

		return "";
	}
}
