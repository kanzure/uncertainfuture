package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.ClickableCurve;
import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.MouseClickListener;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.SimpleLine;
import org.singinst.uf.presenter.SimpleStyle;

public class NormalNodeMetadataContentsFactory implements MouseClickListener {

	private final Collection<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final List<ScalarValueHolder> scalarValueHolders = new ArrayList<ScalarValueHolder>();

	private final ScalarValuePointList curvePointList = new ClickableCurve(this);
	private final ScalarValuePointList pointList5 = new ScalarValuePointList(new SimpleStyle(SimpleColor.GREEN));
	private final ScalarValuePointList pointList50 = new ScalarValuePointList(new SimpleStyle(SimpleColor.BLACK));
	private final ScalarValuePointList pointList95 = new ScalarValuePointList(new SimpleStyle(SimpleColor.RED));
	
	private final ScalarRelation relation;
	private final ScalarSchema mean;
	//private final ScalarSchema median;
	private final ScalarSchema stdDev;
	private final String normalUnits;
	
	public NormalNodeMetadataContentsFactory(Node node, String units, final LineBounds meanBounds, String stdSuffix) {
		String powerStr = meanBounds.displayAsLog() ? " 10 to the " : " ";
		// if we are not displaying as log, it is going to be 'year', so add an 's'
		String plural = meanBounds.displayAsLog() ? "" : "s";
		
		this.normalUnits = units;
		mean = new ScalarSchema(node, ScalarSubIDString.MEAN, meanBounds, meanUnits(), "The median value is " + powerStr, plural, meanBounds, true);
		double maxStdDev = meanBounds.getLength() / 10;
		stdDev = new ScalarSchema(node, ScalarSubIDString.STD_DEV, new LineBounds(0, maxStdDev), units, "std dev is " + stdSuffix + powerStr, "", new LineBounds(0, maxStdDev * 100), true);
		Axis vertical = new Axis(new LineBounds(0, 5 / maxStdDev), false);
		Axis horizontal = new Axis(meanBounds);
		scalarSchemata.add(mean);
		scalarSchemata.add(stdDev);
		scalarValueHolders.add(mean.getScalarValueHolder());
		scalarValueHolders.add(stdDev.getScalarValueHolder());
		
		relation = new ScalarRelation(horizontal, vertical) {

			@Override
			public List<ScalarValuePointList> getPointLists() {
				List<SimplePoint> pointSamples = new ArrayList<SimplePoint>();
				Iterable<Double> samples = 
					meanBounds.getVisualSamples(5001, mean.getScalarValueHolder().getValue(), Arrays.asList(getPercentile5(), getPercentile95()));
				for (double x : samples) {
					pointSamples.add(new SimplePoint(x, yFromX(x)));
				}
				curvePointList.setHypothesisPoints(pointSamples);
				
				pointList5.setHypothesisPoints(verticalLine(getPercentile5()));
				pointList50.setHypothesisPoints(verticalLine(mean.getScalarValueHolder().getValue()));
				pointList95.setHypothesisPoints(verticalLine(getPercentile95()));
				
				List<ScalarValuePointList> retVal = Arrays.asList(curvePointList, pointList5, pointList50, pointList95);
				for (ScalarValuePointList scalarValuePointList : retVal) {
					List<SimplePoint> filteredList = new ArrayList<SimplePoint>();
					for (SimplePoint simplePoint : scalarValuePointList.getHypothesisPoints()) {
						double x = simplePoint.x;
						if (x >= meanBounds.getFirst() && x <= meanBounds.getSecond()) {
							filteredList.add(simplePoint);
						}
					}
					scalarValuePointList.setHypothesisPoints(filteredList);
				}
				return retVal;
			}

			private List<SimplePoint> verticalLine(double x) {
				SimpleLine line = SimpleLine.vertical(x, 0, yFromX(x)); 
				return Arrays.asList(line.p1, line.p2);
			}

			public List<? extends ConclusionReportGenerator> getConclusionGenerators() {
				return Collections.singletonList(new ConclusionReportGenerator() {
					public String getText(ScalarValueHolder scalarValueHolder, double value) {
						boolean power = meanBounds.displayAsLog();
						// if we are not displaying as log, it is going to be 'year', so add an 's'
						String plural = power ? "" : "s";
						String str = "There is a 90% chance of being between ";
						if (power){
							str +=  HtmlUtil.green("10 <sup>" + mean.getBounds().userFormat(getPercentile5()) + "</sup>") +
							" and " +
							HtmlUtil.red("10 <sup>" + mean.getBounds().userFormat(getPercentile95()) + "</sup>") + " " + meanUnits() + plural;
						}
						else {
							str += HtmlUtil.green(mean.getBounds().userFormat(getPercentile5())) + " and " +
							HtmlUtil.red(mean.getBounds().userFormat(getPercentile95())) + " " + meanUnits() + plural;
						}
						
						return str;
					}
				});
			}

			public List<ScalarValueHolder> getScalarValues() {
				return scalarValueHolders;
			}
			
		};
	}

	private Double getPercentile5() {
		return mean.getScalarValueHolder().getValue() - MathUtil.NINETY_FIVE_PERCENTILE * stdDev.getScalarValueHolder().getValue();
	}

	private Double getPercentile95() {
		return mean.getScalarValueHolder().getValue() + MathUtil.NINETY_FIVE_PERCENTILE * stdDev.getScalarValueHolder().getValue();
	}
	
	protected double yFromX(double x) {
		double meanValue = mean.getScalarValueHolder().getValue();
		double stdDevValue = stdDev.getScalarValueHolder().getValue();
		double squaredDistanceFromMean = (meanValue - x) * (meanValue - x);
		double variance = stdDevValue * stdDevValue;
		double functionalY = (1 / Math.sqrt(2 * Math.PI * variance)) * Math.exp(-1 * squaredDistanceFromMean / (2 * variance));
		return functionalY;
	}

	public Collection<? extends ScalarSchema> getScalars() {
		return scalarSchemata;
	}

	public ScalarRelation getRelation() {
		return relation;
	}

	public void mouseClick(SimplePoint point) {
	    double newMean = point.x;
	    double newDensityAtMean = point.y;
		double newVariance = 1 / (2 * Math.PI * newDensityAtMean * newDensityAtMean);
		stdDev.getScalarValueHolder().setValue(Math.sqrt(newVariance));
		mean.getScalarValueHolder().setValue(newMean);

	}

	// TODO5 called from constructor
	protected String meanUnits() {
		return normalUnits;
	}
}
