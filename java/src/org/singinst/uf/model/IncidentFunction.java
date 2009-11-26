package org.singinst.uf.model;

import org.singinst.uf.math.InvertableFunction;

public class IncidentFunction extends InvertableFunction {

	@Override
	public double apply(double x) {
		double log1p = -1 * Math.pow(10, x);
		double probability = -1 * Math.expm1(log1p);
		return probability * 100;
	}

	@Override
	public double invert(double y) {
		double log1p = -1 * Math.log1p(-1 * y/100);
		return Math.log10(log1p);
	}
}
