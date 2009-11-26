package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;

import org.singinst.uf.common.StringUtil;
import org.singinst.uf.presenter.HtmlUtil;

public class MultiplicationCalculation extends Calculation {

	private final Evaluable[] calculationArray;

	public MultiplicationCalculation(String description,
			Evaluable... calculationArray) {
		super(description, false);
		this.calculationArray = calculationArray;
	}

	@Override
	public double rawEvaluate(StringBuilder htmlConsole) {
		double retVal = 1;
		List<String> readableSubCalculationResultList = new ArrayList<String>();
		for (Evaluable subCalculation : calculationArray) {
			double subCalculationResult = subCalculation.evaluate(htmlConsole);
			retVal *= subCalculationResult;
//			String percentage = MathUtil.round(subCalculationResult * 100, 2) + "%"; //FIXME
//			String str = HtmlUtil.htmlcolorFromColor(((Calculation)subCalculation).getColor(), percentage);
			String str = HtmlUtil.htmlcolorFromColor(((Calculation)subCalculation).getColor(), 
					StringUtil.formatProbability(subCalculationResult));
			readableSubCalculationResultList.add(str);
		}
		htmlConsole.append(getDescription() + " = " + StringUtil.join(" * ", readableSubCalculationResultList));
		return retVal;
	}

	
}
