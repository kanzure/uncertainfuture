package org.singinst.uf.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.singinst.uf.math.SimplePoint;

abstract class ProxyCanvas implements GraphCanvas, MouseDragListener, MouseClickListener {
	private final GraphCanvas underlyingCanvas;
	private final Set<MouseDragListener> dragListeners = new HashSet<MouseDragListener>();
	private final Set<MouseClickListener> clickListeners = new HashSet<MouseClickListener>();
	private MouseDragListener selectedDragListener;

	ProxyCanvas(GraphCanvas underlyingCanvas) {
		this.underlyingCanvas = underlyingCanvas;
	}

	public void clear() {
		getUnderlyingCanvas().clear();
	}

	public void drawDecorationPoint(SimplePoint point) {
		getUnderlyingCanvas().drawDecorationPoint(toUnderlyingCanvas(point));
	}

	private SimplePoint toUnderlyingCanvas(SimplePoint point) {
		return proxyToUnderlyingCanvas().apply(point);
	}

	private SimplePoint fromUnderlyingCanvas(SimplePoint point) {
		return proxyToUnderlyingCanvas().invert(point);
	}

	abstract GraphTransform proxyToUnderlyingCanvas();

	public void drawLine(SimpleLine line) {
		getUnderlyingCanvas().drawLine(new SimpleLine(toUnderlyingCanvas(line.p1), toUnderlyingCanvas(line.p2)));
	}

	public void drawCurve(List<SimplePoint> points) {
		List<SimplePoint> underlyingCanvasPoints = new ArrayList<SimplePoint>();
		for (SimplePoint point : points) {
			underlyingCanvasPoints.add(toUnderlyingCanvas(point));
		}
		getUnderlyingCanvas().drawCurve(underlyingCanvasPoints);
	}

	public void addMouseDragListener(final MouseDragListener listener) {
		dragListeners.add(listener);
		getUnderlyingCanvas().addMouseDragListener(this);
	}

	public void addMouseClickListener(final MouseClickListener listener) {
		clickListeners.add(listener);
		getUnderlyingCanvas().addMouseClickListener(this);
	}
	
	public void mouseClick(SimplePoint point) {
		SimplePoint ourPoint = fromUnderlyingCanvas(point);
		for (MouseClickListener listener : clickListeners) {
			listener.mouseClick(ourPoint);
		}
	}

	public void dragTo(SimplePoint point) {
		SimplePoint ourPoint = fromUnderlyingCanvas(point);
		selectedDragListener.dragTo(ourPoint);
	}

	public int mouseDown(SimplePoint point) {
		SimplePoint ourPoint = fromUnderlyingCanvas(point);
		
		SortedMap<Integer, MouseDragListener> listenersByPriority = new TreeMap<Integer, MouseDragListener>();
		for (MouseDragListener listener : dragListeners) {
			 listenersByPriority.put(listener.mouseDown(ourPoint), listener);
		}
		int maxPriority = listenersByPriority.lastKey();
		if (maxPriority > 0) {
			selectedDragListener = listenersByPriority.get(maxPriority);
		}
		return maxPriority;
	}
	
	

	public void invalidate() {
		underlyingCanvas.invalidate();
	}
	
	

	public void pushStyle(SimpleStyle style) {
		underlyingCanvas.pushStyle(style);
	}
	
	

	public void popStyle() {
		underlyingCanvas.popStyle();
	}

	public GraphCanvas getUnderlyingCanvas() {
		return underlyingCanvas;
	}

	public void write(SimplePoint point, CanvasString canvasString) {
		underlyingCanvas.write(toUnderlyingCanvas(point), canvasString);
	}
	
	
}
