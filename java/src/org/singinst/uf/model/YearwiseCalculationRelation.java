package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;

abstract public class YearwiseCalculationRelation extends CalculationRelation {

	public YearwiseCalculationRelation(Node node) {
		super(node);
	}

	@Override
	protected List<Calculation> getCalculations(List<Double> years) {
		List<Calculation> retVal = new ArrayList<Calculation>();
		for (double year : years) {
			retVal.add(getCalculation(year));
		}
		return retVal;
	}
	
	abstract protected Calculation getCalculation(double year);
}
