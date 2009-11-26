package org.singinst.uf.model;

import java.awt.Color;
import org.singinst.uf.presenter.HtmlUtil;

import org.singinst.uf.common.StringUtil;
import org.singinst.uf.math.MathUtil;

public abstract class Calculation implements Evaluable {

	private final String description;
	private StringBuilder htmlConsole = new StringBuilder();
	private final boolean prependDescription;

	public Calculation(String description) {
		this(description, true);
	}

	public Calculation(String description, boolean prependDescription) {
		this.description = description.intern();
		this.prependDescription = prependDescription;
	}

	public String getDescription() {
		return description;
	}
	
	public final double evaluate() {
		return evaluate(getHtmlConsole());
	}
	
	public final double evaluate(StringBuilder htmlConsole) {
		if (prependDescription) {
			htmlConsole.append(description);
		}
		double raw = rawEvaluate(htmlConsole);
		double rounded = MathUtil.round(raw, 12);
//		String percentage = MathUtil.round(raw * 100, 2) + "%"; //FIXME
//		htmlConsole.append(" = " + HtmlUtil.htmlcolorFromColor(getColor(), percentage) + "<br>\n");
		htmlConsole.append(" = " + HtmlUtil.htmlcolorFromColor(getColor(), StringUtil.formatProbability(raw)) + "<br>\n");
		
		return rounded;
	}
	
	protected abstract double rawEvaluate(StringBuilder htmlConsole);

	/**
	 * 	Returns the color for this calculation, so that we consistently display the correct one.
	 * 
	 * @return
	 * 	The color to display this calculation
	 */
	public Color getColor() {
		return Color.BLACK;
	}
	
	public StringBuilder getHtmlConsole() {
		return htmlConsole;
	}

	public void setHtmlConsole(StringBuilder htmlConsole) {
		this.htmlConsole = htmlConsole;
	}

}
