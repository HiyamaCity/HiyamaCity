package de.hiyamacity.util;

public class Util {

	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}
}
