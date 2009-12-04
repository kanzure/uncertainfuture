package org.singinst.uf.view;

import java.net.MalformedURLException;
import java.net.URL;

import org.singinst.uf.common.LogUtil;

public class ViewUtil {
	private static Boolean isApple = null;
	
	public static boolean renderExponentsAsSuperscript() {
		return !runningOnApple();
	}
	
	private static URL getAppleUrl() throws MalformedURLException {
		return new URL("http://www.apple.com/");
	}
	
	public static boolean runningOnApple() {
		if (isApple == null) {
			try {
				// this seems surprisingly slow on FireFox, try to only call it once
				String vendorUrlString = System.getProperty("java.vendor.url");
				LogUtil.info("Vendor URL: " + vendorUrlString);
				isApple = getAppleUrl().equals(new URL(vendorUrlString));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				isApple = false;
				//			throw new RuntimeException(e);
			}
		}
		return isApple;
	}
}
