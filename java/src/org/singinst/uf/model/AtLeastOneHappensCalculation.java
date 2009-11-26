package org.singinst.uf.model;


public class AtLeastOneHappensCalculation extends CompositeCalculation {

	public AtLeastOneHappensCalculation(String description,
			Evaluable... calculations) {
		super(description, calculations);
	}

	@Override
	protected double rawEvaluate(StringBuilder htmlConsole) {
		double probabilityOfNot = 1;
		for (Evaluable calculation : getCalculations()) {
			probabilityOfNot *= (1 - calculation.evaluate(htmlConsole));
		} 
		htmlConsole.append(getDescription());
		return 1 - probabilityOfNot;
	}
}
