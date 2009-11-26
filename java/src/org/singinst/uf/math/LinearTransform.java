package org.singinst.uf.math;

import org.singinst.uf.presenter.LineBounds;

public class LinearTransform extends InvertableFunction {

	private final double scale;
	private final double offset;

	public LinearTransform(LineBounds from, LineBounds to) {
		scale = to.getLength() / from.getLength();
		offset = to.getFirst() - scale * from.getFirst();
		assert apply(from.getFirst()) == to.getFirst();
		assert apply(from.getSecond()) == to.getSecond();
	}

	@Override
	public final double apply(double x) {
		return scale * x + offset;
	}

	@Override
	public double invert(double y) {
		return (y - offset) / scale;
	}

}
