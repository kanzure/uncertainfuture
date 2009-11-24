package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;

import org.singinst.uf.math.InvertableFunction;
import org.singinst.uf.presenter.AxisSample;
import org.singinst.uf.presenter.LineBounds;

public class LogBounds extends LineBounds {

	private final double majorTickSpace;
	private final double minorTickSpace;
	private final double zero;
	private final double firstMajorTick;
	private final InvertableFunction toDisplay;

	public LogBounds(double firstMajorTick, double zero, double first, double second, double majorTickSpace, double minorTickSpace) {
		super(first, second);
		this.firstMajorTick = firstMajorTick;
		this.zero = zero;
		this.majorTickSpace = majorTickSpace;
		this.minorTickSpace = minorTickSpace;
		toDisplay = new PowerFunction(zero);
	}

	@Override
	public Iterable<AxisSample> getAxisSamples(boolean majorTick) {
		List<AxisSample> samples = new ArrayList<AxisSample>();
		double space;
		double display;
		if (majorTick) {
			space = majorTickSpace;
			display = firstMajorTick - zero;
		} else {
			space = minorTickSpace;
			display = Math.pow(10, getFirst());
			display += (minorTickSpace - display % minorTickSpace);
		}
		while (display <= Math.pow(10, getSecond())) {
			if (majorTick) {
				if (zero + display == 2050) {
					addSample(samples, 2010 - zero);
					addSample(samples, 2030 - zero);
				}
				if ((zero + display != 2350) && (zero + display != 2450)) {
					addSample(samples, display);
				}
			} else {
				samples.add(new AxisSample(Math.log10(display)));
			}
			display += space;
		}
		return samples;
	}
	
	

	@Override
	public InvertableFunction toDisplay() {
		return toDisplay;
	}
	
	@Override
	public String userFormat(double value) {
		return "" + Math.round(toDisplay.apply(value));
	}
	
	@Override
	public double constrain(double rounded) {
		double bounded = super.constrain(rounded);
		double display = toDisplay.apply(bounded);
		double intDisplay = Math.round(display);
		return toDisplay.invert(intDisplay);
	}

	private boolean addSample(List<AxisSample> samples, double display) {
		return samples.add(new AxisSample(Math.log10(display), getCanvasString(zero + display)));
	}
}
