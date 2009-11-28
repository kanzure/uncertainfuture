package org.singinst.uf.common;

import java.util.Collection;

import org.singinst.uf.math.MathUtil;

public class StringUtil {
	// Show probabilities as 25.57% or 0.2557?
	private static final boolean PROBABILITY_AS_PERCENTAGE = true;
	
	// Decimal digits to show for probabilities (i.e. 2 => 25.57% or 0.26)
	private static final int PROBABILITY_DECIMALS = 2;
	
	// Decimal digits to show for multipliers (i.e. 2 => 3.44x)
	private static final int MULTIPLIER_DECIMALS = 2;
	
	
	public static String join(String delimiter, Collection<String> collection) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String string : collection) {
			if (!first) {
				builder.append(delimiter);
			}
			first = false;
			builder.append(string);
		}
		return builder.toString();
	}
	
	/**
	 * Format a probability value for display. 
	 * @param p
	 * @return
	 * @author henrik
	 */
	public static String formatProbability(double p) {
		String value;
	
		if (PROBABILITY_AS_PERCENTAGE) 
			value = MathUtil.round(p * 100, PROBABILITY_DECIMALS) + "%";
		else
			value = MathUtil.round(p, PROBABILITY_DECIMALS) + "";
		return value;
	}
	
	/**
	 * Format a multiplier value for display. 
	 * @param v
	 * @return
	 * @author henrik
	 */
	public static String formatMultiplier(double v) {
		return MathUtil.round(v, MULTIPLIER_DECIMALS) + "x";
	}
	
	
}
