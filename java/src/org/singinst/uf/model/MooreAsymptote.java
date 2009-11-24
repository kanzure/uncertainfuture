package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.singinst.uf.common.LogUtil;
import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.DraggableLine;
import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.SimpleStyle;

public class MooreAsymptote {
	private final ScalarSchema scalarSchema;
	private final LineBounds domain;
	private final ScalarValuePointList curvePointList;
	private final DraggableLine asymptotePointList;

	public MooreAsymptote(Node node, LineBounds domain, LineBounds asymptoteRange, NotablePercentile percentile) {
		asymptotePointList = new MooreDraggableLine(this, percentile);
		SimpleStyle style = new SimpleStyle(percentile.getColor());
		curvePointList = new ScalarValuePointList(style);
		this.domain = domain;
		int percentileValue = percentile.getValue();
		String name = "k" + percentileValue;
		scalarSchema = new ScalarSchema(node, name, asymptoteRange, "(log) FLOPS/$", HtmlUtil.style(style, percentile.getText("early", "late") + " stop: 10 to the "), "", null, true);
	}

	public Collection<? extends ScalarValuePointList> getPointLists() {
		return Arrays.asList(asymptotePointList(), curvePointList());
	}

	private ScalarValuePointList curvePointList() {
		List<SimplePoint> pointSamples = new ArrayList<SimplePoint>();
		for (double x : domain.getSpanningSamples(101)) {
			pointSamples.add(new SimplePoint(x, yFromX(x)));
		}
		curvePointList.setHypothesisPoints(pointSamples);
		return curvePointList;
	}

	private static final long T0 = 2008;
	private static final double P0_NON_LOG = 10280000;
	private static final double P0_LOG = Math.log(P0_NON_LOG);	
	private static final double DOUBLING_TIME_IN_YEARS = 1.5;
	private static final double R = Math.log(2) / DOUBLING_TIME_IN_YEARS;
	private double yFromX(double x) {
		double exponentialGrowthFactor = Math.exp(R * (x - T0));
		double kNoLog = Math.pow(10, getConjecture().getValue());
		double numerator = (kNoLog) * P0_NON_LOG * exponentialGrowthFactor;
		double denominator = (kNoLog) + P0_NON_LOG * (exponentialGrowthFactor - 1);
		double functionalYNoLog = numerator / denominator;
		return Math.log10(functionalYNoLog);
	}

	private ScalarValuePointList asymptotePointList() {
		double y = getConjecture().getValue();
		asymptotePointList.setHypothesisPoints(Arrays.asList(
				new SimplePoint(domain.getLowerBound(), y),
				new SimplePoint(domain.getUpperBound(), y)));
		return asymptotePointList;
	}

	public ScalarValueHolder getConjecture() {
		return scalarSchema.getScalarValueHolder();
	}

	public ScalarSchema getScalar() {
		return scalarSchema;
	}

	public void setY(double y) {
		scalarSchema.getScalarValueHolder().setValue(y);	
	}

	public static double unboundedLnFlopsPerDollarAtYear(double year) {
		double unboundedLnFlopsPerDollar = P0_LOG + (R * (year - T0));
		//LogUtil.info("at year " + year + ", natural log unbounded flops per dollar is " + unboundedLnFlopsPerDollar);
		return unboundedLnFlopsPerDollar;
	}
}
