package com.cb.pro.tme.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.joda.time.DateTime;

public class Utils {
	public static String isoDateTime() {
		String crTm = new DateTime().toString();

		return crTm.substring(0, crTm.lastIndexOf("+"))+"Z";		
	}

	public static double round(double value, int places) {
		if(places < 0) throw new IllegalArgumentException();

		return (new BigDecimal(value)).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}

	public static boolean isValidUUID(String uuid) {
		if(uuid == null || uuid.isEmpty()) return false;

		try {
			UUID.fromString(uuid);

	        return true;
		}
		catch(Exception e) {			
		}

		return false;
	}

	public static String signGenerate(String secretKey, String requestPath, String method, String body, String timestamp) {
		try {
			final Mac sha256 = (Mac) Mac.getInstance("HmacSHA256").clone();
			sha256.init(new SecretKeySpec(
				java.util.Base64.getDecoder().decode(secretKey),
				Mac.getInstance("HmacSHA256").getAlgorithm()
			));

			return java.util.Base64.getEncoder().encodeToString(sha256.doFinal((timestamp + method.toUpperCase() + requestPath + body).getBytes()));
		}
		catch(Exception e) {
			// e.printStackTrace();
		}

		return "";
	}
}
