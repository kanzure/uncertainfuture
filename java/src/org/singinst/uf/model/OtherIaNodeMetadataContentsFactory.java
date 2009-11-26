package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.LineBounds;

public class OtherIaNodeMetadataContentsFactory {

	private final YearExtremeNodeMetadataContentsFactory extreme;
	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final ScalarRelation relation;
	
	private final List<PercentileDraggableLine> pointLists = new ArrayList<PercentileDraggableLine>();
	
	private final List<ScalarValueHolder> conjectureList = new ArrayList<ScalarValueHolder>();
	
	
	public OtherIaNodeMetadataContentsFactory(Node node) {
		final LineBounds range = new LineBounds(0, 2, true);
		final LineBounds domain = new LineBounds(ModelUtil.EARLIEST_YEAR, ModelUtil.LATEST_YEAR);
		extreme = new OtherIaExtreme(node, range, ModelUtil.LATEST_YEAR);
		scalarSchemata.addAll(extreme.getScalars());
		for (ScalarSchema scalarSchema : scalarSchemata) {
			conjectureList.add(scalarSchema.getScalarValueHolder());
		}

		for (final NotablePercentile percentile : NotablePercentile.values()) {
			PercentileDraggableLine pointList = new PercentileDraggableLine(percentile) {

				@Override
				public void dragTo(SimplePoint point) {
					OtherIaNodeMetadataContentsFactory.this.dragTo(percentile, point);
				}};
			pointLists.add(pointList);
		}

		relation = new ScalarRelation(new Axis(domain), new Axis(range)) {

			@Override
			public List<? extends ScalarValuePointList> getPointLists() {
				for (PercentileDraggableLine incidentLine : pointLists) {
					List<SimplePoint> points = new ArrayList<SimplePoint>();
					points.add(new SimplePoint(domain.getFirst(), range.getFirst()));
					points.add(new SimplePoint(extreme.getYear(), 
							extreme.getScalarSchema(incidentLine.getPercentile()).getScalarValueHolder().getValue()));
					incidentLine.setHypothesisPoints(points);
				}
				return pointLists;
			}

			public List<? extends ConclusionReportGenerator> getConclusionGenerators() {
				List<ConclusionReportGenerator> retVal = new ArrayList<ConclusionReportGenerator>();
				retVal.add(extreme.getConclusionGenerator());
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
		SimplePoint anchorPoint = new SimplePoint(ModelUtil.EARLIEST_YEAR, 0);
		double newExtremeY = MathUtil.interpolate(
				anchorPoint, point, ModelUtil.LATEST_YEAR);
		extreme.getScalarSchema(percentile).getScalarValueHolder().setValue(newExtremeY);
	}
}
