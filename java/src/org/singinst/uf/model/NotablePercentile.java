package org.singinst.uf.model;

import org.singinst.uf.math.MathUtil;

public enum NotablePercentile {
	PERCENTILE5(5, SimpleColor.GREEN, 1, -1 * MathUtil.NINETY_FIVE_PERCENTILE), 
	PERCENTILE50(50, SimpleColor.BLACK, 2, 0), 
	PERCENTILE95(95, SimpleColor.RED, 1, MathUtil.NINETY_FIVE_PERCENTILE);

	private final int value;
	private final SimpleColor color;
	private final int priority;
	private final double offset;
	
	private NotablePercentile(int value, SimpleColor color, int priority, double offset) {
		this.value = value;
		this.color = color;
		this.priority = priority;
		this.offset = offset;
	}

	public int getPriority() {
		return priority;
	}

	public int getValue() {
		return value;
	}

	public SimpleColor getColor() {
		return color;
	}

	public double getOffset() {
		return offset;
	}

	/**
	 * 	Returns a human readable description of whether or not this is extremely high, mid or extremely low probability.
	 * 
	 * @param adjectiveSmall
	 * 		The adjective to use if it is astonishingly (blank) (5th percentile)
	 * @param adjectiveLarge
	 * 		The adjective to use if it is astonishingly (blank) (95th percentile)
	 * @return
	 * 		String describing the range this NotablePercentile covers.
	 */
	public String getText(String adjectiveSmall, String adjectiveLarge) {
		switch (value) {
			case 5:  return "Astonishingly " + adjectiveSmall;// + " (5th percentile)";
			case 50: return "Median";
			case 95: return "Astonishingly " + adjectiveLarge;// + " (95th percentile)";			
			default: return "";
		}
	}
}
