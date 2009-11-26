package org.singinst.uf.common;


public class LogUtil {

	public static void error(Exception e) {
		e.printStackTrace();
		
	}

	public static void info(String string) {
		System.err.println(string);
	}

}
