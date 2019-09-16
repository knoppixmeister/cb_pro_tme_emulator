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
	public List<ObOrder> getAllOrders(
		@RequestHeader("CB-ACCESS-KEY") String cbAccessKey,
		@RequestHeader("CB-ACCESS-SIGN") String cbAccessSign,
		@RequestHeader("CB-ACCESS-TIMESTAMP") String cbAccessTimestamp,
		@RequestHeader("CB-ACCESS-PASSPHRASE") String cbAccessPassphrase
	)
	{
		/*
		 	400|Bad Request
			{"message":"CB-ACCESS-SIGN header is required"}

			400|Bad Request
			{"message":"invalid signature"}

		 	401|Unauthorized
			{"message":"CB-ACCESS-KEY header is required"}

		 	400|Bad Request
			{"message":"Invalid API Key"}
	
			400|Bad Request
			{"message":"CB-ACCESS-TIMESTAMP header is required"}

			400|Bad Request
			{"message":"invalid timestamp"}

			400|Bad Request
			{"message":"CB-ACCESS-PASSPHRASE header is required"}

			400|Bad Request
			{"message":"Invalid Passphrase"}
	
		 */

		return Application.ORDERS;
	}

	@GetMapping(value = "/orders/{id}")
	public ResponseEntity<String> getOrder(
		@RequestHeader("CB-ACCESS-KEY") String cbAccessKey,
		@RequestHeader("CB-ACCESS-SIGN") String cbAccessSign,
		@RequestHeader("CB-ACCESS-TIMESTAMP") String cbAccessTimestamp,
		@RequestHeader("CB-ACCESS-PASSPHRASE") String cbAccessPassphrase,

		@PathVariable(name="id", required = false) String id
	)
	{
		if(id != null && !id.isEmpty()) {
			getAllOrders(cbAccessKey, cbAccessSign, cbAccessTimestamp, cbAccessPassphrase);

			/*
			if(!Utils.isValidUUID(id)) {
				return new ResponseEntity<>("{\"message\":\"Invalid order id\"}", HttpStatus.BAD_REQUEST);
			}
			*/
		}

		for(ObOrder o : Application.ORDERS) {
			if(o.id.equals(id)) {
				return new ResponseEntity<String>(
					new Moshi.Builder().build().adapter(ObOrder.class).toJson(o),
					HttpStatus.OK
				);
			}
		}

		return new ResponseEntity<String>("{\"message\":\"NotFound\"}", HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/orders")
	public Order placeOrder(
		@RequestHeader("CB-ACCESS-KEY") String cbAccessKey,
		@RequestHeader("CB-ACCESS-SIGN") String cbAccessSign,
		@RequestHeader("CB-ACCESS-TIMESTAMP") String cbAccessTimestamp,
		@RequestHeader("CB-ACCESS-PASSPHRASE") String cbAccessPassphrase,

		@RequestBody String body
	)
	{
		System.out.println("BD: "+body);

		/*
			400|Bad Request
			{"message":"Invalid order_type lemit"}

			400|Bad Request
			{"message":"Invalid order_type LIMIT"}

			400|Bad Request
			{"message":"Invalid order_type limIt"}

		----------------------------------------------------------------------------------

			400|Bad Request
			{"message":"size is too small. Minimum size is 0.00100000"}

			400|Bad Request
			{"message":"size must be a number"}

			400|Bad Request
			{"message":"Requires size"}

		----------------------------------------------------------------------------------

			400|Bad Request
			{"message":"Requires price"}

			400|Bad Request
			{"message":"price must be a number"}
			
		----------------------------------------------------------------------------------
		
			// case sensisitive "side" name
		
			400|Bad Request
			{"message":"Requires side"}

			400|Bad Request
			{"message":"Invalid side buY"}

		-----------------------------------------------------------

			400|Bad Request
			{"message":"Product not found"}

		-----------------------------------------------------------

			400|Bad Request
			{"message":"Unexpected token s in JSON at position 96"}

		-----------------------------------------------------------------

			400|Bad Request
			{"message":"Invalid time_in_force GTCi"}

		-----------------------------------------------------------------

			400|Bad Request
			{"message":"Invalid client_oid"}

		 */

		String wsEventData = "";

		Application.TME_EVENTS_QUEUE.add(wsEventData);

		return null;
	}

	@DeleteMapping(value = "/orders")
	public ResponseEntity<String> cancelOrders(
		@RequestHeader("CB-ACCESS-KEY") String cbAccessKey,
		@RequestHeader("CB-ACCESS-SIGN") String cbAccessSign,
		@RequestHeader("CB-ACCESS-TIMESTAMP") String cbAccessTimestamp,
		@RequestHeader("CB-ACCESS-PASSPHRASE") String cbAccessPassphrase
	)
	{

		return new ResponseEntity<String>("{}", HttpStatus.OK);
	}

	@DeleteMapping(value="/orders/{id}")
	public ResponseEntity<String> cancelOrders(
		@RequestHeader("CB-ACCESS-KEY") String cbAccessKey,
		@RequestHeader("CB-ACCESS-SIGN") String cbAccessSign,
		@RequestHeader("CB-ACCESS-TIMESTAMP") String cbAccessTimestamp,
		@RequestHeader("CB-ACCESS-PASSPHRASE") String cbAccessPassphrase,

		@PathVariable(name = "id") final String id
	)
	{
		String realId = id;

		if(id != null && !id.isEmpty()) {
			if(!Utils.isValidUUID(id)) {
				return new ResponseEntity<String>("{\"message\":\"Invalid order id\"}", HttpStatus.BAD_REQUEST);
			}
		}
		else {
			if(id.contains(":")) {
				realId = realId.substring(realId.indexOf(":"));

				if(!Utils.isValidUUID(realId)) {
					return new ResponseEntity<String>("{\"message\":\"Invalid order id\"}", HttpStatus.BAD_REQUEST);
				}
				if(!id.startsWith("client")) {
					return new ResponseEntity<String>("{\"message\":\"Invalid id namespace\"}", HttpStatus.BAD_REQUEST);
				}
			}
			else {
				if(!Utils.isValidUUID(realId)) {
					return new ResponseEntity<String>("{\"message\":\"Invalid order id\"}", HttpStatus.BAD_REQUEST);
				}
			}
		}

		for(ObOrder o : Application.ORDERS) {
			if(o.id.equalsIgnoreCase(realId)) {
				return new ResponseEntity<String>("[\""+o.id+"\"]", HttpStatus.OK);
			}
		}

		/*

			500|Internal Server Error
			{"message":"Internal server error"}

			404|Not Found
			{"message":"order not found"}

			400|Bad Request
			{"message":"Invalid order id"}

			400|Bad Request
			{"message":"Invalid id namespace"}

		 */

		if(id.startsWith("client:")) {
			return new ResponseEntity<String>("{\"message\":\"Internal server error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else return new ResponseEntity<String>("{\"message\":\"order not found\"}", HttpStatus.NOT_FOUND);
	}
}
