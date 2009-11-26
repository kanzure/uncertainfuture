package org.singinst.uf.presenter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.math.SimplePoint;

public abstract class DraggableLine extends ScalarValuePointList implements MouseDragListener {
	private final int selectionPriority;
	
	public DraggableLine(int selectionPriority, SimpleStyle style) {
		super(style);
		this.selectionPriority = selectionPriority;
	}
	
	public void setLine(SimpleLine line) {
		setHypothesisPoints(Arrays.asList(line.p1, line.p2));
	}
	
	public int getSelectionPriority() {
		return selectionPriority;
	}

	@Override
	public void draw(GraphCanvas canvas) {
		super.draw(canvas);
		canvas.addMouseDragListener(this);
	}

	@Override
	public void setHypothesisPoints(List<SimplePoint> hypothesisPoints) {
		if (hypothesisPoints.size() != 2) {
			throw new IllegalArgumentException();
		}
		super.setHypothesisPoints(hypothesisPoints);
	}

	public abstract void dragTo(SimplePoint point);

	public int mouseDown(SimplePoint point) {
		double y = MathUtil.interpolate(getHypothesisPoints().get(0), getHypothesisPoints().get(1), point.x);
		if (Math.abs(y - point.y) < 0.5) {
			return selectionPriority;
		} else {
			return 0;
		}
	}
	
	public static final Comparator<DraggableLine> PRIORITY_COMPARATOR = new Comparator<DraggableLine>() {

		public int compare(DraggableLine o1, DraggableLine o2) {
			return ((Integer)o1.getSelectionPriority()).compareTo(o2.getSelectionPriority());
		}
		
	};
}
