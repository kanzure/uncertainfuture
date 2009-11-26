package org.singinst.uf.math;

public abstract class InvertableFunction {

	public static final InvertableFunction IDENTITY = new InvertableFunction() {

		@Override
		public double apply(double x) {
			return x;
		}

		@Override
		public double invert(double y) {
			return y;
		}
		
	};

	public abstract double apply(double x);
	public abstract double invert(double y);
	
	public InvertableFunction invert() {
		return new InvertedFunction(this);
	}

}
