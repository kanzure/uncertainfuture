package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.LineBounds;

public class HazardRateNodeMetadataContentsFactory {

	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final ScalarRelation relation;
	private final Map<Double, YearExtremeNodeMetadataContentsFactory> extremeByYear = new HashMap<Double, YearExtremeNodeMetadataContentsFactory>();
	
	private final List<PercentileDraggableLine> pointLists = new ArrayList<PercentileDraggableLine>();
	
	private final List<ScalarValueHolder> conjectureList = new ArrayList<ScalarValueHolder>();
	
	private static final double MID_YEAR = MathUtil.average(ModelUtil.EARLIEST_YEAR, ModelUtil.LATEST_YEAR);

	public HazardRateNodeMetadataContentsFactory(Node node) {
		IncidentBounds incidentBounds = new IncidentBounds();
		LineBounds yearBounds = new LineBounds(ModelUtil.EARLIEST_YEAR, ModelUtil.LATEST_YEAR);
		final List<IncidentExtremeNodeMetadataContentsFactory> extremes = IncidentExtremeNodeMetadataContentsFactory.createList(node, incidentBounds, yearBounds);
		for (YearExtremeNodeMetadataContentsFactory extreme : extremes) {
			extremeByYear.put(extreme.getYear(), extreme);
			scalarSchemata.addAll(extreme.getScalars());
		}
		for (ScalarSchema scalarSchema : scalarSchemata) {
			conjectureList.add(scalarSchema.getScalarValueHolder());
		}
		
		for (NotablePercentile percentile : NotablePercentile.values()) {
			PercentileDraggableLine pointList = new IncidentLine(this, percentile);
			pointLists.add(pointList);
		}

		relation = new ScalarRelation(new Axis(yearBounds), new Axis(incidentBounds)) {

			@Override
			public List<? extends ScalarValuePointList> getPointLists() {
				for (PercentileDraggableLine incidentLine : pointLists) {
					List<SimplePoint> points = new ArrayList<SimplePoint>();
					for (YearExtremeNodeMetadataContentsFactory extreme : extremes) {
						points.add(new SimplePoint(extreme.getYear(), 
								extreme.getScalarSchema(incidentLine.getPercentile()).getScalarValueHolder().getValue()));
					}
					incidentLine.setHypothesisPoints(points);
				}
				return pointLists;
			}

			public List<? extends ConclusionReportGenerator> getConclusionGenerators() {
				List<ConclusionReportGenerator> retVal = new ArrayList<ConclusionReportGenerator>();
				for (YearExtremeNodeMetadataContentsFactory extreme : extremes) {
					retVal.add(extreme.getConclusionGenerator());
				}
				return retVal;
			}

			public List<ScalarValueHolder> getScalarValues() {
				return conjectureList ;
			}
		};
	}

	public Collection<? extends ScalarSchema> getScalars() {
		return scalarSchemata;
	}

	public ScalarRelation getRelation() {
		return relation;
	}

	public void dragTo(NotablePercentile percentile, SimplePoint point) {
		boolean selectLeft = point.x < MID_YEAR;
		double year = selectLeft ? ModelUtil.EARLIEST_YEAR : ModelUtil.LATEST_YEAR;
		double anchorYear = selectLeft ? ModelUtil.LATEST_YEAR : ModelUtil.EARLIEST_YEAR;
		YearExtremeNodeMetadataContentsFactory extreme = extremeByYear.get(year);
		if (extreme == null) {
			throw new NullPointerException("" + year);
		}
		YearExtremeNodeMetadataContentsFactory anchorExtreme = extremeByYear.get(anchorYear);
		if (anchorExtreme == null) {
			throw new NullPointerException("" + year);
		}
		SimplePoint anchorPoint = new SimplePoint(
				anchorYear, anchorExtreme.getScalarSchema(percentile).getScalarValueHolder().getValue());
		double newExtremeY = MathUtil.interpolate(
				anchorPoint, point, year);
		extreme.getScalarSchema(percentile).getScalarValueHolder().setValue(newExtremeY);
	}
}
