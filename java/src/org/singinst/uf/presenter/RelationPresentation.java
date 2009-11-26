package org.singinst.uf.presenter;

import java.util.List;

import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.model.ScalarValueHolder;
import org.singinst.uf.model.ScalarRelation;
import org.singinst.uf.model.ValueListener;

public class RelationPresentation implements ValueListener {

	private static final SimplePoint ORIGIN = new SimplePoint(0,0);
	private static final SimplePoint UPPER_LEFT = new SimplePoint(0,1);
	private static final SimplePoint LOWER_RIGHT = new SimplePoint(1,0);
	private final GraphCanvas viewCanvas;
	private final ScalarRelation relation;
	
	private final GraphCanvas unitPlusMarginCanvas;
	private final GraphCanvas hypothesisCanvas; 

	public RelationPresentation(GraphCanvas viewCanvas, ScalarRelation relation) {
		this.viewCanvas = viewCanvas;
		this.relation = relation;
		
		unitPlusMarginCanvas = new ScaledCanvas(viewCanvas,
				new PlaneBounds(-0.1, 1.06, -0.1, 1.02));
		GraphCanvas unitCanvas = new ClippedCanvas(unitPlusMarginCanvas, new PlaneBounds(0, 1, 0, 1));
		hypothesisCanvas = new ScaledCanvas(unitCanvas, new PlaneBounds(relation.getDomain(), relation.getRange()));
		
		for (ScalarValueHolder scalarValueHolder : relation.getScalarValues()) {
			scalarValueHolder.addUpdateListener(this);
		}
	}
	

	public Runnable draw() {
		viewCanvas.clear();
		
		for (SimplePoint point : relation.decorationPoints) {
			hypothesisCanvas.pushStyle(SimpleStyle.DECORATION_POINT);
			hypothesisCanvas.drawDecorationPoint(point);
			hypothesisCanvas.popStyle();
		}

		drawAxes();
		
		List<? extends ScalarValuePointList> pointLists = relation.getPointLists();
		for (ScalarValuePointList pointList : pointLists) {
			if (pointList.getHypothesisPoints() == null) {
				return pointList.updater();
			}
			pointList.draw(hypothesisCanvas);
		}
		return null;
	}


	private void drawAxes() {
		unitPlusMarginCanvas.pushStyle(SimpleStyle.AXIS);
		if (relation.getDomain().isVisibleAxis()) {
			unitPlusMarginCanvas.drawLine(new SimpleLine(ORIGIN, LOWER_RIGHT));			
		}
		if (relation.getRange().isVisibleAxis()) {
			unitPlusMarginCanvas.drawLine(new SimpleLine(ORIGIN, UPPER_LEFT));
		}

		AxisDirection.X.draw(hypothesisCanvas, relation);
		AxisDirection.Y.draw(hypothesisCanvas, relation);

		unitPlusMarginCanvas.popStyle();
	}

	public void fireUpdate(double value) {
		viewCanvas.invalidate();
	}
}
