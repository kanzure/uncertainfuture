package org.singinst.uf.presenter;


public class ScaledCanvas extends ProxyCanvas {

	private final PlaneBounded ourBounds;

	public ScaledCanvas(GraphCanvas underlyingCanvas, PlaneBounded us) {
		super(underlyingCanvas);
		this.ourBounds = us.getPlaneBounds();
	}
	
	protected GraphTransform proxyToUnderlyingCanvas() {
		return new GraphTransform(ourBounds, getUnderlyingCanvas());
	}

	public PlaneBounds getPlaneBounds() {
		return ourBounds.getPlaneBounds();
	}
}
