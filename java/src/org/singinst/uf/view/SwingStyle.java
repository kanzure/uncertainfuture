package org.singinst.uf.view;

import java.awt.Color;
import java.awt.Graphics2D;

import org.singinst.uf.model.SimpleColor;
import org.singinst.uf.presenter.SimpleStyle;

public class SwingStyle {

	private final Color color;
	private final boolean dottedLine;

	public SwingStyle(Graphics2D g2) {
		color = g2.getColor();
		// TODO5
		dottedLine = false;
	}

	public SwingStyle(SimpleStyle style) {
		SimpleColor simpleColor = style.getColor();
		color = new SimpleColor.Visitor<Color>() {

			@Override
			public Color visit_BLACK() {
				return Color.BLACK;
			}

			@Override
			public Color visit_GREEN() {
				return Color.GREEN;
			}

			@Override
			public Color visit_LIGHT_GRAY() {
				return Color.LIGHT_GRAY;
			}

			@Override
			public Color visit_RED() {
				return Color.RED;
			}
		}.visit(simpleColor);
		
		dottedLine = style.isDottedLine();
	}
	
	public Color getColor() {
		return color;
	}

	public boolean isDottedLine() {
		return dottedLine;
	}
}
