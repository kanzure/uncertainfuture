package org.singinst.uf.model;

//import java.util.List;


public class DisruptionRelation extends YearwiseCalculationRelation {

	public DisruptionRelation(Node node) {
		super(node);
	}

	@Override
	protected Calculation getCalculation(double year) {
		return new AtLeastOneHappensCalculation("Probability that either AI, or a science-disrupting catastrophe, disrupts business as usual by year " + (int) year,
				ScalarValueHolder.findById(NodeIDString.C1_8, ScalarSubIDString.yearProbabilityID(year)),
				ScalarValueHolder.findById(NodeIDString.C1_9, ScalarSubIDString.yearProbabilityID(year)));
	}	
}
