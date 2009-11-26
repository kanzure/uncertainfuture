package org.singinst.uf.presenter;

import java.awt.Color;

import org.singinst.uf.model.SimpleColor;

public class HtmlUtil {

	public static String green(Object value) {
		return color("green", value);
	}
	
	/**
	 * 	Takes a Color and an object, and wraps it around a span html element, and adds the color to it.
	 * 
	 * @param c
	 * 		Color to display value in
	 * @param value
	 * 		Value to display in color c
	 * @return
	 * 		HTML code.
	 */
	public static String htmlcolorFromColor(Color c, Object value){
		return color("#" + Integer.toHexString((c.getRGB() & 0xffffff) | 0x1000000).substring(1) , value);
	}

	private static String color(String color, Object value) {
		return "<html><span color=" + color + ">" + value + "</span>";
	}

	public static String red(Object value) {
		return color("red", value);
	}

	public static String style(SimpleStyle style, String string) {
		return color(htmlColor(style), string);
	}

	private static String htmlColor(SimpleStyle style) {
		return new SimpleColor.Visitor<String>() {

			@Override
			public String visit_BLACK() {
				return "black";
			}

			@Override
			public String visit_GREEN() {
				return "green";
			}

			@Override
			public String visit_LIGHT_GRAY() {
				return "light gray";
			}

			@Override
			public String visit_RED() {
				return "red";
			}
			
		}.visit(style.getColor());
	}
}
