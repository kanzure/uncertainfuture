package org.singinst.uf.presenter;

import org.singinst.uf.model.SimpleColor;

public class SimpleStyle {

	private final SimpleColor color;
	private final boolean dottedLine;

	public SimpleStyle(SimpleColor color) {
		this(color, false);
	}

	public SimpleStyle(SimpleColor color, boolean dottedLine) {
		this.color = color;
		this.dottedLine = dottedLine;
	}

	public static final SimpleStyle DECORATION_POINT = new SimpleStyle(SimpleColor.LIGHT_GRAY);
	public static final SimpleStyle AXIS = new SimpleStyle(SimpleColor.LIGHT_GRAY);
	public static final SimpleStyle PLAIN = new SimpleStyle(SimpleColor.BLACK);

	public SimpleColor getColor() {
		return color;
	}

	public boolean isDottedLine() {
		return dottedLine;
	}

}
