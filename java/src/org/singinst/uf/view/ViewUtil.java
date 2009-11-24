package org.singinst.uf.view;

import java.net.MalformedURLException;
import java.net.URL;

public class ViewUtil {
	private static Boolean isApple = isApple();
	
	public static boolean renderExponentsAsSuperscript() {
		return !isApple;
	}
	
	private static URL apple() throws MalformedURLException {
		return new URL("http://apple.com/");
	}
	
	private static boolean isApple() {
		try {
			// this seems surprisingly slow on FireFox, try to only call it once
			return apple().equals(new URL(System.getProperty("java.vendor.url")));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
