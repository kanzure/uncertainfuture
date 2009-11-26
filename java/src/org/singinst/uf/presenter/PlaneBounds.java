package org.singinst.uf.presenter;


public class PlaneBounds implements PlaneBounded {

	private LineBounds boundsX;
	private LineBounds boundsY;

	public PlaneBounds(double left, double right, double bottom, double top) {
		boundsX = new LineBounds(left, right);
		boundsY = new LineBounds(bottom, top);
	}

	public PlaneBounds(LineBounded boundedX, LineBounded boundedY) {
		this.boundsX = boundedX.getLineBounds();
		this.boundsY = boundedY.getLineBounds();
	}

	public LineBounds getBoundsX() {
		return boundsX;
	}

	public LineBounds getBoundsY() {
		return boundsY;
	}

	public PlaneBounds getPlaneBounds() {
		return this;
	}

}
