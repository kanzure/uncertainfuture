package org.singinst.uf.presenter;

import org.singinst.uf.math.InvertableFunction;
import org.singinst.uf.math.LinearTransform;
import org.singinst.uf.math.SimplePoint;

public class GraphTransform {

	public static final GraphTransform IDENTITY = new GraphTransform(LinearTransform.IDENTITY, LinearTransform.IDENTITY);
	private final InvertableFunction transformX;
	private final InvertableFunction transformY;

	public GraphTransform(PlaneBounded from, PlaneBounded to) {
		transformX = new LinearTransform(from.getPlaneBounds().getBoundsX(), to.getPlaneBounds().getBoundsX());
		transformY = new LinearTransform(from.getPlaneBounds().getBoundsY(), to.getPlaneBounds().getBoundsY());
	}

	public GraphTransform(InvertableFunction transformX,
			InvertableFunction transformY) {
		this.transformX = transformX;
		this.transformY = transformY;
	}

	public SimplePoint apply(SimplePoint point) {
		return new SimplePoint(transformX.apply(point.x), transformY.apply(point.y));
	}

	public GraphTransform invert() {
		return new GraphTransform(transformX.invert(), transformY.invert());
	}

	public SimplePoint invert(SimplePoint point) {
		return invert().apply(point);
	}
	
	
}
