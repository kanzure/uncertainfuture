package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;

import org.singinst.uf.math.InvertableFunction;
import org.singinst.uf.presenter.AxisSample;
import org.singinst.uf.presenter.CanvasString;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.NumericEntry;

public class IncidentBounds extends LineBounds {
	
	private static final InvertableFunction toDisplay = new IncidentFunction();

	private static final double LOWER_DISPLAY = 0.001;
	private static final double UPPER_DISPLAY = 99;

	public IncidentBounds() {
		super(fromDisplay(LOWER_DISPLAY), fromDisplay(UPPER_DISPLAY));
	}

	@Override
	public Iterable<AxisSample> getAxisSamples(boolean majorTick) {
		return samples(0.001, 0.01, 0.1, 1, 10, 50, 99);
	}

	private Iterable<AxisSample> samples(double... displays) {
		List<AxisSample> samples = new ArrayList<AxisSample>();
		for (double display : displays) {
			samples.add(new AxisSample(fromDisplay(display), 
					new CanvasString("" + NumericEntry.formatAsScalar(display))));
		}
		return samples;
	}
	
	@Override
	public InvertableFunction toDisplay() {
		return toDisplay;
	}
	
	@Override
	public String userFormat(double value) {
		return "" + NumericEntry.formatAsScalar(toDisplay.apply(value));
	}
	
	private static double fromDisplay(double display) {
		return toDisplay.invert(display);
	}
}
