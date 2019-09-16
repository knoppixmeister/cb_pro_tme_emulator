package com.cb.pro.tme.emu;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiRestOrdersController {
	@GetMapping(value = "/orders")
	public String orders() {

		return "";
	}

	@GetMapping(value = "/orders/{id}")
	public List<Order> orders(@PathVariable(name = "id") String id) {
		;

		return null;
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
