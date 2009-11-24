package org.singinst.uf.model;

import org.singinst.uf.math.InvertableFunction;

public class PowerFunction extends InvertableFunction {

	private final double zero;

	public PowerFunction(double zero) {
		this.zero = zero;
	}

	@Override
	public double apply(double x) {
		return Math.pow(10, x) + zero;
	}

	@Override
	public double invert(double y) {
		return Math.log10(y - zero);
	}
}
