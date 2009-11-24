package org.singinst.uf.presenter;

import java.text.DecimalFormat;
import java.text.Format;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.model.ScalarSchema;

public class NumericEntry {
	private static String SCALAR_FORMAT_STRING;
	static {
		SCALAR_FORMAT_STRING = "##0.";
		for (int i = 0; i < ScalarSchema.getMaxDecimalDigits(); i++) {
			SCALAR_FORMAT_STRING += "#";
		}
	}
	public static Format getScalarFormat() {
		return new DecimalFormat(SCALAR_FORMAT_STRING);
	}
	public static String formatAsScalar(double value) {
		return getScalarFormat().format(value);
	}
	
	private static int MAX_COLUMNS = 4 + ScalarSchema.getMaxDecimalDigits();

	public static int maxColumns() {
		return MAX_COLUMNS;
	}
	public static double round(double roundMe) {
		return MathUtil.round(roundMe, ScalarSchema.getMaxDecimalDigits());
	}
}
