package org.singinst.uf.presenter;

import org.singinst.uf.math.SimplePoint;

public class ClickableCurve extends ScalarValuePointList implements MouseClickListener {
	private final MouseClickListener listener;

	public ClickableCurve(MouseClickListener listener) {
		this.listener = listener;
		
	}
	
	public void draw(GraphCanvas canvas) {
		super.draw(canvas);
		canvas.addMouseClickListener(listener);
	}

	public void mouseClick(SimplePoint point) {
		
	}
}
