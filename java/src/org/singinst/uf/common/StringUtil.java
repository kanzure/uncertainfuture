package org.singinst.uf.common;

import java.util.Collection;

import org.singinst.uf.math.MathUtil;

public class StringUtil {
	// Show probabilities as 25.57% or 0.2557?
	private static final boolean PROBABILITY_AS_PERCENTAGE = true;
	
	// Decimal digits to show (i.e. 2 => 25.57% or 0.26)
	private static final int PROBABILITY_DECIMALS = 2;
	
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
}
