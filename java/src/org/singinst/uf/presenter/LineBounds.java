package org.singinst.uf.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.singinst.uf.math.InvertableFunction;
import org.singinst.uf.math.MathUtil;

public class LineBounds implements LineBounded {
	private final double first;
	private final double second;
	private final boolean displayAsLog;

	public LineBounds(double first, double second) {
		this(first, second, false);
	}

	public LineBounds(double first, double second, boolean displayAsLog) {
		if (first == second) {
			throw new IllegalArgumentException();
		}
		this.first = first;
		this.second = second;
		this.displayAsLog = displayAsLog;
	}

	public double getLength() {
		return getSecond() - getFirst();
	}

	public double getLowerBound() {
		return getFirst();
	}

	public double getUpperBound() {
		return getSecond();
	}

	public LineBounds getLineBounds() {
		return this;
	}

	public double getMidpoint() {
		return constrain((getLowerBound() + getUpperBound()) / 2);
	}

	public Iterable<Double> getSpanningSamples(int sampleCount) {
		List<Double> samples = new ArrayList<Double>();
		for (int i = 0; i < sampleCount; i++) {
			samples.add(getUpperBound() * i / (sampleCount - 1)
					+ getLowerBound() * (sampleCount - i - 1) / (sampleCount - 1)
					);
		}
		return samples;
	}

	public Iterable<Double> getVisualSamples(int maxSamples, double start, Collection<Double> requiredPoints) {
		double sampleSizeLowerBound = getLength() / maxSamples;
		double sampleSize = Math.pow(10, Math.ceil(Math.log10(sampleSizeLowerBound)));
		SortedSet<Double> samples = new TreeSet<Double>();
		for (double i = start; i <= getUpperBound(); i += sampleSize) {
			samples.add(i);
		}
		for (double i = start - sampleSize; i >= getLowerBound(); i -= sampleSize) {
			samples.add(i);
		}
		samples.addAll(requiredPoints);
		return samples;
	}

	public Iterable<AxisSample> getAxisSamples(boolean majorTick) {
		int maxReadableSamples = majorTick ? 10 : 100;
		List<AxisSample> retVal = new ArrayList<AxisSample>();
		for (double value : getVisualSamples(maxReadableSamples, getLowerBound(), Collections.<Double>emptyList())) {
			if (majorTick) {
				retVal.add(new AxisSample(value, getCanvasString(value)));
			} else {
				retVal.add(new AxisSample(value));				
			}
		}
		return retVal;
	}

	protected CanvasString getCanvasString(double x) {
		if (displayAsLog) {
			return new CanvasString("" + 10, NumericEntry.formatAsScalar(x));
		} else {
			return new CanvasString(NumericEntry.formatAsScalar(x));
		}
	}

	public double getFirst() {
		return first;
	}

	public double getSecond() {
		return second;
	}
	
	public boolean displayAsLog(){
		return displayAsLog;
	}

	public InvertableFunction toDisplay() {
		return InvertableFunction.IDENTITY;
	}

	public String userFormat(double value) {
			return NumericEntry.getScalarFormat().format(value);
	}

	public double constrain(double rounded) {
		return MathUtil.bound(rounded, getFirst(), getSecond());
	}
}
