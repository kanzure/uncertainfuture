package org.singinst.uf.model;

public class ScalarSubIDString {

	public static final String PROBABILITY = "probability";
	public static final String MEAN = "mean";
	public static final String STD_DEV = "stdDev";
	public static final String MEAN_INIT = "meanInit";
	public static final String STD_DEV_INIT = "stdDevInit";
	public static final String MEAN_SLOPE = "meanSlope";
	public static final String STD_DEV_SLOPE = "stdDevSlope";
	
	public static final String K95 = "k95";
	public static final String K50 = "k50";
	public static final String K5 = "k5";

	public static String yearPercentileID(double year, double percentile) {
		return "y" + year + "k" + percentile;
	}

	public static String yearProbabilityID(double year) {
		return "y" + year + "p";
	}
	
}
