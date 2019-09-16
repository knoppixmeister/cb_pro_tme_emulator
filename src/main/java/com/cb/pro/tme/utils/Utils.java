package com.cb.pro.tme.utils;

import java.util.UUID;

public class Utils {
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
}
