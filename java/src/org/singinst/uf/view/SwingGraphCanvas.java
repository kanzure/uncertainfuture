package org.singinst.uf.view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.JPanel;

import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.CanvasString;
import org.singinst.uf.presenter.GraphCanvas;
import org.singinst.uf.presenter.MouseClickListener;
import org.singinst.uf.presenter.MouseDragListener;
import org.singinst.uf.presenter.PlaneBounds;
import org.singinst.uf.presenter.SimpleLine;
import org.singinst.uf.presenter.SimpleStyle;

public class SwingGraphCanvas implements GraphCanvas, MouseListener, MouseMotionListener {

	private Graphics2D g2;
	private Stack<SwingStyle> styleStack;
	private Dimension size;
	private PlaneBounds bounds;
	private final Set<MouseDragListener> dragListeners = new HashSet<MouseDragListener>();
	private final Set<MouseClickListener> clickListeners = new HashSet<MouseClickListener>();
	private JPanel panel;
	private MouseDragListener selectedDragListener;
	
	public SwingGraphCanvas() {
//		MainWindow.pane.addChangeListener(new ChangeListener() {
//
//			public void stateChanged(ChangeEvent e) {
////				dragListeners.clear();
////				clickListeners.clear();
//			}
//			
//		});
	}
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public void setGraphics(Graphics2D g2) {
		this.g2 = g2;
		this.styleStack = new Stack<SwingStyle>();
		styleStack.push(new SwingStyle(g2));
	}
	
	public void setSize(Dimension size) {
		this.size = size;
		this.bounds = new PlaneBounds(0, size.width, size.height, 0);		
	}

	public void clear() {
		g2.clearRect(0,0,size.width,size.height);
	}

	public void drawLine(SimpleLine line) {
		g2.draw(new Line2D.Double(point(line.p1), point(line.p2)));
	}

	private Point2D point(SimplePoint p) {
		return new Point2D.Double(p.x, p.y);
	}

	public void drawDecorationPoint(SimplePoint point) {
		double dotDiameter = size.width / 200;
		double dotRadius = dotDiameter / 2;
		g2.draw(new Ellipse2D.Double(point.x - dotRadius, point.y - dotRadius, dotDiameter, dotDiameter));
	}

	public PlaneBounds getPlaneBounds() {
		return bounds;
	}

	public void drawCurve(List<SimplePoint> points) {
		if (points.isEmpty()) {
			return;
		}
		GeneralPath curve = new GeneralPath();
		Iterator<SimplePoint> iterator = points.iterator();
		curveMoveTo(curve, iterator.next());
		while (iterator.hasNext()) {
			curveLineTo(curve, iterator.next());
		}
		g2.draw(curve);
		
	}

	private void curveLineTo(GeneralPath curve, SimplePoint point) {
		curve.lineTo((float) point.x, (float) point.y);
	}

	private void curveMoveTo(GeneralPath curve, SimplePoint point) {
		curve.moveTo((float) point.x, (float) point.y);
	}

	public void addMouseClickListener(MouseClickListener listener) {
		clickListeners.add(listener);
		if (dragListeners.size() + dragListeners.size() > 1) {
			throw new UnsupportedOperationException();
		}
		panel.addMouseListener(this);
	}
	
	public void addMouseDragListener(MouseDragListener listener) {
		dragListeners.add(listener);
		if (dragListeners.size() + clickListeners.size() > 1) {
			throw new UnsupportedOperationException();
		}
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		selectedDragListener = null;
	}

	public void mousePressed(MouseEvent e) {
		for (MouseClickListener listener : clickListeners) {
			listener.mouseClick(simplePoint(e.getPoint()));
		}
		if (dragListeners.size() > 0) {
			MouseDragListener dragListener = dragListeners.iterator().next();
			int priority = dragListener.mouseDown(simplePoint(e.getPoint()));
			if (priority > 0) {
				selectedDragListener = dragListener;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (selectedDragListener != null) {
			selectedDragListener.dragTo(simplePoint(e.getPoint()));
			selectedDragListener = null;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (selectedDragListener != null) {
			selectedDragListener.dragTo(simplePoint(e.getPoint()));
		}
	}

	private SimplePoint simplePoint(Point point) {
		return new SimplePoint(point.getX(), point.getY());
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void invalidate() {
		if (panel != null) {
			panel.repaint();
		}
	}



	public void popStyle() {
		styleStack.pop();
		setStyle(styleStack.peek());
	}



	private void setStyle(SwingStyle style) {
		g2.setColor(style.getColor());
		if (style.isDottedLine()) {
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] {2}, 0));
		} else {
			g2.setStroke(new BasicStroke());
		}
	}

	public void pushStyle(SimpleStyle style) {
		SwingStyle swingStyle;
		if (style == null) {
			swingStyle = styleStack.peek();
		} else {
			swingStyle = new SwingStyle(style);
		}
		styleStack.push(swingStyle);
		setStyle(swingStyle);
	}



	public void write(SimplePoint point, CanvasString canvasString) {
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout(getAttributedCharacterIterator(canvasString), frc);
			textLayout.draw(g2, (float) point.x, (float) point.y);
		}

	private AttributedCharacterIterator getAttributedCharacterIterator(
			CanvasString canvasString) {
		String main = canvasString.getMain();
		String optionalPower = canvasString.getOptionalPower();
		AttributedString string;
		if (optionalPower == null) {
			string = new AttributedString(main);
		} else {
			if (ViewUtil.renderExponentsAsSuperscript()) {
				String plainText = main + optionalPower;
				string = new AttributedString(plainText);
				string.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, main.length(), plainText.length());
			} else {
				string = new AttributedString(main + "^" + optionalPower);				
			}
		}
		string.addAttribute(TextAttribute.SIZE, 18f);
		return string.getIterator();
	}

}
