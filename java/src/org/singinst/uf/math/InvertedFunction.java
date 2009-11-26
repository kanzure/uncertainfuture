package org.singinst.uf.math;

public class InvertedFunction extends InvertableFunction {

	private final InvertableFunction originalFunction;

	public InvertedFunction(InvertableFunction originalFunction) {
		this.originalFunction = originalFunction;
	}

	@Override
	public double apply(double x) {
		return originalFunction.invert(x);
	}

	@Override
	public double invert(double y) {
		return originalFunction.apply(y);
	}

	@Override
	public InvertableFunction invert() {
		return originalFunction;
	}
}
