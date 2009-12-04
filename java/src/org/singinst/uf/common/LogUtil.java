package org.singinst.uf.common;


public class LogUtil {

	// Show debug information
	private static final boolean debug = true;

	public static void error(Exception e) {
		e.printStackTrace();
		
	}

	public static void info(String string) {
		if (debug)
			System.err.println(string);
	}

}
