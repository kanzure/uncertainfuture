package org.singinst.uf.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.singinst.uf.model.ScalarRelation;
import org.singinst.uf.presenter.RelationPresentation;

public class Graph {

	private final SwingGraphCanvas swingGraphCanvas;
	private final RelationPresentation relationPresentation;
	private final JPanel panel;

	@SuppressWarnings("serial")
	public Graph(final ScalarRelation relation) {
		swingGraphCanvas = new SwingGraphCanvas();
		relationPresentation = new RelationPresentation(swingGraphCanvas, relation);
		panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				swingGraphCanvas.setGraphics((Graphics2D) g);
				swingGraphCanvas.setSize(getSize());
				final Runnable updater = relationPresentation.draw();
				if (updater != null) {
					new Thread(new Runnable() {
						public void run() {
							updater.run();
							panel.invalidate();
						}
					}).start();
				}
			}
		};
		panel.setMinimumSize(new Dimension(500, 400));
		panel.setPreferredSize(new Dimension(500, 400));
		swingGraphCanvas.setPanel(panel);

	}

	public Component getPanel() {
		return panel;
//				for (GraphLine graphLine : modelBean.getInitializedGraphLines()) {
//					double startDrawingPoint = graphLine.getStartDrawingPoint();
//					drawGraph(g2, graphLine, startDrawingPoint, 1);
//					drawGraph(g2, graphLine, startDrawingPoint, 0);
//				}

//				g2.draw(GraphUtil.line(new Line2D.Double(new UnitGraph(xUserSpace(0), yUserSpace(1), xUserSpace(1), yUserSpace(1)));
//				if (!model.getModelBean().getUnitSmallTickListY().isEmpty()) {
//					g2.draw(new Line2D.Double(xUserSpace(0), yUserSpace(1), xUserSpace(0), yUserSpace(0)));
//				}
//
//				for (double smallTickX : model.getModelBean().getUnitSmallTickListX()) {
//					g2.draw(new Line2D.Double(xUserSpace(smallTickX), yUserSpace(1 + 0.02), xUserSpace(smallTickX), yUserSpace(1 - 0.02)));
//				}
//
//				FontRenderContext frc = g2.getFontRenderContext();
//				for (LabeledTick labelX : model.getModelBean().getUnitLabeledTickListX()) {
//					double unitX = labelX.getUnitCoordinate();
//					g2.draw(new Line2D.Double(xUserSpace(unitX), yUserSpace(1 + 0.05), xUserSpace(unitX), yUserSpace(1 - 0.05)));
//					TextLayout textLayout = new TextLayout(labelX.getAttributedCharacterIterator(), frc);
//					textLayout.draw(g2, (float) xUserSpace(unitX), (float) yUserSpace(1 - 0.05));
//				}
//
//				for (double smallTickY : model.getModelBean().getUnitSmallTickListY()) {
//					g2.draw(new Line2D.Double(xUserSpace(-0.02), yUserSpace(smallTickY), xUserSpace(0.02), yUserSpace(smallTickY)));
//				}
//
//				for (LabeledTick labelY : model.getModelBean().getUnitLabeledTickListY()) {
//					double unitY = labelY.getUnitCoordinate();
//					g2.draw(new Line2D.Double(xUserSpace(-0.05), yUserSpace(unitY), xUserSpace(0.05), yUserSpace(unitY)));
//					TextLayout textLayout = new TextLayout(labelY.getAttributedCharacterIterator(), frc);
//					textLayout.draw(g2, (float) xUserSpace(-0.05), (float) yUserSpace(unitY));
//				}
//
//				double dotSize = 1;
//				for (GraphObservationPoint gop : model.getModelBean().decorationPointSet()) {
//					g2.draw(new Ellipse2D.Double(
//							xUserSpace(gop.getUnitX()) - dotSize / 2, yUserSpace(gop.getUnitY()) - dotSize / 2, dotSize, dotSize));
//				}
//
//				g2.setFont(g2.getFont().deriveFont(Font.ITALIC));
//				String xUnitLabel = model.getModelBean().xUnitLabel();
//
//				g2.drawString(xUnitLabel, (float) xUserSpace(0.5), (float) yUserSpace(1 + 0.10));
//
//				String yUnitLabel = model.getModelBean().yUnitLabel();
//				g2.drawString(yUnitLabel, (float) xUserSpace(-0.10), (float) yUserSpace(0.5));
	}

}
