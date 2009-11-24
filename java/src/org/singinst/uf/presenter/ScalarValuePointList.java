package org.singinst.uf.presenter;

import java.util.List;

import org.singinst.uf.math.SimplePoint;


public class ScalarValuePointList {

	private List<SimplePoint> hypothesisPoints;
	private final SimpleStyle style;

	public ScalarValuePointList() {
		this(null);
	}
	
	public ScalarValuePointList(SimpleStyle style) {
		this.style = style;
	}

	public void setHypothesisPoints(List<SimplePoint> hypothesisPoints) {
		this.hypothesisPoints = hypothesisPoints;
	}

	public void draw(GraphCanvas canvas) {
		canvas.pushStyle(style);
		canvas.drawCurve(getHypothesisPoints());
		canvas.popStyle();
	}

	public List<SimplePoint> getHypothesisPoints() {
		return hypothesisPoints;
	}

	public Runnable updater() {
		throw new UnsupportedOperationException();
	}
}
