package org.singinst.uf.presenter;

import java.util.List;

import org.singinst.uf.math.SimplePoint;

public interface GraphCanvas extends PlaneBounded {

	void clear();

	void drawLine(SimpleLine line);

	void drawDecorationPoint(SimplePoint point);

	void drawCurve(List<SimplePoint> points);

	void addMouseDragListener(MouseDragListener listener);
	void addMouseClickListener(MouseClickListener listener);

	void invalidate();

	void pushStyle(SimpleStyle style);
	void popStyle();

	void write(SimplePoint point, CanvasString canvasString);

}
