package org.singinst.uf.model;

import java.awt.Color;
import java.util.List;

import org.singinst.uf.math.MathUtil;



public class AiRelation extends YearwiseCalculationRelation {

	public boolean displayHtml;
	
	public AiRelation(Node node) {
		super(node);
		this.displayHtml = displayHtml;
	}

	@Override
	protected Calculation getCalculation(double year) {
		return new MultiplicationCalculation("Probability of either sort of AI by year " + (int) year, 
				probabilityAiIfAiIsPossible(year),
				probabilityAiIsPossible());

	}

	private Calculation probabilityAiIfAiIsPossible(final double year) {
		return new Calculation("Probability of AI (if AI is possible) by year " + (int) year) {

			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				return MathUtil.expc(
					  MathUtil.clog(
							ScalarValueHolder.findById(NodeIDString.C1_5, ScalarSubIDString.yearProbabilityID(year)).evaluate(new StringBuilder())
						  / probabilityAiIsPossible().evaluate()
					  )
					+ MathUtil.clog(getNonNeuromorphicAiCalculationForYear(year).evaluate())
				);
			}
			
			@Override
			public Color getColor() {
				return Color.BLUE;
			}
		};
/*				ScalarValueHolder.get(NodeIDString.C1_5, yearScalarName(year)),
				getNonNeuromorphicAiCalculationForYear(year)) {
			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				double probabilityOfNot = 1;
				for (Evaluable calculation : getCalculations()) {
					probabilityOfNot *= (1 - calculation.evaluate(htmlConsole));
				}
				htmlConsole.append(getDescription());
				return 1 - probabilityOfNot;
			}
		};*/
	}
	private Calculation getNonNeuromorphicAiCalculationForYear(final double year) {
		return new Calculation("Probability that non-neuromorphic AI will be created by year " + year, false) {

			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				double mean = ScalarValueHolder.findById(NodeIDString.Q1_6, ScalarSubIDString.MEAN).getValue();
				double stdDev = ScalarValueHolder.findById(NodeIDString.Q1_6, ScalarSubIDString.STD_DEV).getValue();
				return MathUtil.cumulativeNormalDistribution(mean, stdDev, Math.log10(year - ModelUtil.ANCHOR_FAR_YEAR));
			}
		};
	}
}
