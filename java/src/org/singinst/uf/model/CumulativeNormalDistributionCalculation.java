package org.singinst.uf.model;

import java.awt.Color;

import org.singinst.uf.math.MathUtil;

public class CumulativeNormalDistributionCalculation extends Calculation {

	private final Evaluable mean;
	private final Evaluable stdDev;
	private final double x;
	private final Color color;

	/**
	 * 
	 * @param description
	 * @param mean
	 * @param stdDev
	 * @param x
	 * @param display
	 * 		The color to display the result of this calculation.
	 */
	public CumulativeNormalDistributionCalculation(String description, Evaluable mean, Evaluable stdDev, double x, Color display) {
		super(description, false);
		this.mean = mean;
		this.stdDev = stdDev;
		this.x = x;
		this.color = display;
	}

	@Override
	public double rawEvaluate(StringBuilder htmlConsole) {
		StringBuilder nullBuilder = new StringBuilder();
		double meanValue = mean.evaluate(nullBuilder);
		double stdDevValue = stdDev.evaluate(nullBuilder);
		double retVal = MathUtil.cumulativeNormalDistribution(
				meanValue, stdDevValue, x);
		//htmlConsole.append("Cumulative probability (mean = " + meanValue + "; stddev = " + stdDevValue + ") of " + getDescription() + " at " + x);
		htmlConsole.append(getDescription().replace("\'", ""));
		return retVal;
	}
	
	@Override
	public Color getColor(){
		return color;
	}

}
