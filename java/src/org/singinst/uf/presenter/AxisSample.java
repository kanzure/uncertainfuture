package org.singinst.uf.presenter;

public class AxisSample {

	private final double value;
	private final CanvasString label;
	
	public AxisSample(double value) {
		this(value, null);
	}

	public AxisSample(double value, CanvasString label) {
		this.value = value;
		this.label = label;
	}

	public double getValue() {
		return value;
	}

	public CanvasString getLabel() {
		return label;
	}

}
