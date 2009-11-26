package org.singinst.uf.presenter;

import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.model.Axis;
import org.singinst.uf.model.ScalarRelation;

public abstract class AxisDirection {

	public static final AxisDirection X = new AxisDirection() {

		@Override
		protected void drawPerpindicularLine(GraphCanvas canvas,
				double parallelOffset, double perpindicularOffset1,
				double perpindicularOffset2) {
			canvas.drawLine(SimpleLine.vertical(parallelOffset, perpindicularOffset1, perpindicularOffset2));
		}

		@Override
		protected Axis getAxis(ScalarRelation relation) {
			return relation.getDomain();
		}

		@Override
		protected Axis getPerpindicularAxis(ScalarRelation relation) {
			return relation.getRange();
		}

		@Override
		protected void write(GraphCanvas hypothesisCanvas, ScalarRelation relation,
				double parallelOffset, 
				double centParallel, double centPerpindicular,
				CanvasString canvasString) {
			hypothesisCanvas.write(
					new SimplePoint(parallelOffset - 1.5 * centParallel, 
							relation.getRange().getLineBounds().getLowerBound() - 5 * centPerpindicular), 
					canvasString);	
		}
	};
	
	public static final AxisDirection Y = new AxisDirection() {

		@Override
		protected void drawPerpindicularLine(GraphCanvas canvas,
				double parallelOffset, double perpindicularOffset1,
				double perpindicularOffset2) {
			canvas.drawLine(SimpleLine.horizontal(perpindicularOffset1, perpindicularOffset2, parallelOffset));
		}

		@Override
		protected Axis getAxis(ScalarRelation relation) {
			return relation.getRange();
		}

		@Override
		protected Axis getPerpindicularAxis(ScalarRelation relation) {
			return relation.getDomain();
		}

		@Override
		protected void write(GraphCanvas hypothesisCanvas, ScalarRelation relation,
				double parallelOffset, 
				double centParallel, double centPerpindicular,
				CanvasString canvasString) {
			hypothesisCanvas.write(
					new SimplePoint(relation.getDomain().getLineBounds().getLowerBound() - 5 * centPerpindicular, 
							parallelOffset - 2 * centParallel),
					canvasString);
		}
	};
	
	public void draw(GraphCanvas hypothesisCanvas, ScalarRelation relation) {
		if (!getAxis(relation).isVisibleAxis()) {
			return;
		}
		
		LineBounds parallelBounds = getAxis(relation).getLineBounds();
		LineBounds perpindicularBounds = getPerpindicularAxis(relation).getLineBounds();
		double centParallel = parallelBounds.getLength() / 100;
		double centPerpindicular = perpindicularBounds.getLength() / 100;

		for (AxisSample axisSample : parallelBounds.getAxisSamples(false)) {
			double parallelOffset = axisSample.getValue();
			drawPerpindicularLine(hypothesisCanvas,
							parallelOffset, 
							perpindicularBounds.getLowerBound() - centPerpindicular, 
							perpindicularBounds.getLowerBound() + centPerpindicular);
		}
		hypothesisCanvas.pushStyle(SimpleStyle.PLAIN);
		for (AxisSample axisSample : parallelBounds.getAxisSamples(true)) {
			double parallelOffset = axisSample.getValue();
			drawPerpindicularLine(hypothesisCanvas,
							parallelOffset, 
							perpindicularBounds.getLowerBound() - centPerpindicular, 
							perpindicularBounds.getLowerBound() + centPerpindicular);
			write(hypothesisCanvas, relation, parallelOffset, centParallel, centPerpindicular, axisSample.getLabel());
		}
		hypothesisCanvas.popStyle();	
	}
	
	protected abstract void write(GraphCanvas hypothesisCanvas, ScalarRelation relation,
			double parallelOffset, 
			double centParallel, double centPerpindicular,
			CanvasString canvasString);

	protected abstract void drawPerpindicularLine(GraphCanvas canvas,
			double parallelOffset, double perpindicularOffset1, double perpindicularOffset2);

	protected abstract Axis getAxis(ScalarRelation relation);
	protected abstract Axis getPerpindicularAxis(ScalarRelation relation);

}
