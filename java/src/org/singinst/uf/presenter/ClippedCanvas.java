package org.singinst.uf.presenter;


public class ClippedCanvas extends ProxyCanvas {

	private final PlaneBounds clipBounds;

	public ClippedCanvas(GraphCanvas underlyingCanvas,
			PlaneBounds clipBounds) {
		super(underlyingCanvas);
		this.clipBounds = clipBounds;
	}

	public PlaneBounds getPlaneBounds() {
		return clipBounds;
	}

	GraphTransform proxyToUnderlyingCanvas() {
		return GraphTransform.IDENTITY;
	}

}
