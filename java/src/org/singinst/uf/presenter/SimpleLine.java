package org.singinst.uf.presenter;

import org.singinst.uf.math.SimplePoint;

public class SimpleLine {

	public final SimplePoint p1;
	public final SimplePoint p2;

	public SimpleLine(SimplePoint p1, SimplePoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public static SimpleLine vertical(double x, double y1, double y2) {
		return new SimpleLine(new SimplePoint(x, y1), new SimplePoint(x, y2));
	}

	public static SimpleLine horizontal(double x1, double x2, double y) {
		return new SimpleLine(new SimplePoint(x1, y), new SimplePoint(x2, y));
	}

}
