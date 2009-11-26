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

public class ResearchersNodeMetadataContentsFactory {

	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final ScalarRelation relation;
	private final Map<Double, YearExtremeNodeMetadataContentsFactory> extremeByYear = new HashMap<Double, YearExtremeNodeMetadataContentsFactory>();
	
	private final List<PercentileDraggableLine> pointLists = new ArrayList<PercentileDraggableLine>();
	
	private final List<ScalarValueHolder> conjectureList = new ArrayList<ScalarValueHolder>();
	private final LineBounds domain;
	
	public ResearchersNodeMetadataContentsFactory(Node node) {
		final LineBounds range = new LineBounds(1, 7, true);
		domain = new LineBounds(0, ModelUtil.LATEST_IA_DELAY);
		final List<ResearcherExtreme> extremes = ResearcherExtreme.createList(node, range, domain);
		for (YearExtremeNodeMetadataContentsFactory extreme : extremes) {
			extremeByYear.put(extreme.getYear(), extreme);
			scalarSchemata.addAll(extreme.getScalars());
		}
		for (ScalarSchema scalarSchema : scalarSchemata) {
			conjectureList.add(scalarSchema.getScalarValueHolder());
		}
		
		for (final NotablePercentile percentile : NotablePercentile.values()) {
			PercentileDraggableLine pointList = new PercentileDraggableLine(percentile) {

				@Override
				public void dragTo(SimplePoint point) {
					ResearchersNodeMetadataContentsFactory.this.dragTo(percentile, point);
				}};
			pointLists.add(pointList);

		}
		relation = new ScalarRelation(new Axis(domain), new Axis(range)) {

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
		boolean selectLeft = point.x < domain.getMidpoint();
		double year = selectLeft ? domain.getFirst() : domain.getSecond();
		double anchorYear = selectLeft ? domain.getSecond() : domain.getFirst();
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
