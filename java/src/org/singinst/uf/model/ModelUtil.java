package org.singinst.uf.model;



public class ModelUtil {
	protected static final int EARLIEST_YEAR = 2010;
	protected static final int LATEST_YEAR = 2070;
	public static final int EARLIEST_FAR_YEAR = 2010;
	public static final int LATEST_FAR_YEAR = 2500;
	public static final int EARLIEST_IA_YEAR = 2010;
	public static final int LATEST_IA_YEAR = 2050;
	
	public static final int ANCHOR_FAR_YEAR = 1950;
	public static final int LATEST_IA_DELAY = 50;
	public static final int RESEARCH_CAREER_DELAY = 20;
	
	public static final int LATEST_UNACCELERATED_YEAR = 5000;

	public static final double[] BASIC_MODEL_YEARS;
	static {
		BASIC_MODEL_YEARS = new double[LATEST_YEAR-EARLIEST_YEAR+1];
		for (int i=EARLIEST_YEAR; i<=LATEST_YEAR; ++i) {
			BASIC_MODEL_YEARS[i-EARLIEST_YEAR] = i;
		}
	}
	public static final double YEAR_STEPSIZE = 0.5;
	// TODO document this parameter in UI
	public static final double UNENHANCED_RESEARCHERS = 100000d;
}
