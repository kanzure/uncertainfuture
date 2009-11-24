package org.singinst.uf.model;

import java.awt.Color;
import java.util.List;

import org.singinst.uf.math.MathUtil;

public class NeuromorphicAiRelation extends YearwiseCalculationRelation {

	public NeuromorphicAiRelation(Node node) {
		super(node);
	}

	@Override
	protected Calculation getCalculation(final double year) {
		Calculation agiPossible =
				probabilityAiIsPossible();
		Calculation sufficientHardware = 
			new Calculation("Probability of sufficient hardware in " + (int) year) {

				protected double rawEvaluate(StringBuilder htmlConsole) {
					return probabilityOfSufficientHardware(year);
				}
				
				@Override
				public Color getColor(){
					return Color.BLUE;
				}
		};
		Calculation sufficientBrainImaging = probabilityOfSufficientBrainImaging(year);
		return new MultiplicationCalculation(
				"Probability of a neuromorphic AI on or before " + (int) year, 
				agiPossible, 
				sufficientHardware, 
				sufficientBrainImaging
				);
	}

	private Calculation probabilityOfSufficientBrainImaging(final double year) {
		return new CumulativeNormalDistributionCalculation(
					"'Probability of sufficient brain imaging in " + year + "'", 
					ScalarValueHolder.findById(NodeIDString.Q1_5, ScalarSubIDString.MEAN),
					ScalarValueHolder.findById(NodeIDString.Q1_5, ScalarSubIDString.STD_DEV),
					Math.log10(year - ModelUtil.ANCHOR_FAR_YEAR), 
					Color.RED);
	}
	
	private double probabilityOfSufficientHardware(double year) {
		double unboundedLogFlopsPerDollarForYear = MooreAsymptote.unboundedLnFlopsPerDollarAtYear(year);
		double meanLogComputingEfficiencyNeeded = meanComputingEfficiencyNeeded() * Math.log(10);
		double stdDevLogComputingEfficiencyNeeded = stdDevComputingEfficiencyNeeded() * Math.log(10);
		double muX = muX() * Math.log(10);
		double sigmaX = sigmaX() * Math.log(10);
		
		double numerator = 0;
		double denominator = 0;
		double z;
		org.singinst.uf.common.LogUtil.info(String.format("probabilityOfSufficientHardware called at year %5g, time %20.15g", (double)year, ((double) System.currentTimeMillis()) / 1000));
		if (meanLogComputingEfficiencyNeeded + 10.0 * stdDevLogComputingEfficiencyNeeded > unboundedLogFlopsPerDollarForYear) {
			/* brute force midpoint integral, hope for Gaussian Euler-Maclaurin edge term suppression */
			double logComputingEfficiency;
			for (int s = -100; s <= 100; s += 1) {
				z = (double)s/10;
				double slice = Math.exp(-0.5 * z * z);
				logComputingEfficiency = h(muX + sigmaX * z, unboundedLogFlopsPerDollarForYear);
				numerator += slice * MathUtil.cumulativeNormalDistribution(meanLogComputingEfficiencyNeeded, stdDevLogComputingEfficiencyNeeded, logComputingEfficiency);
				denominator += slice;
//				org.singinst.uf.common.LogUtil.info(String.format("at year %5g, z = %+8g (x = %+12.7g) d = %10g, num = %g, denom = %g", (double)year, z, sigmaX() * z, d(sigmaX() * z + muX(), year), numerator, denominator));
			}
			org.singinst.uf.common.LogUtil.info(String.format("needed = %8g +/- %8g, bound = %8g +/- %8g, unlimited = %8g", meanLogComputingEfficiencyNeeded, stdDevLogComputingEfficiencyNeeded, muX, sigmaX, unboundedLogFlopsPerDollarForYear));
		} else {
			double logComputingEfficiencyUpperBoundMinimum;
			for (int s = -100; s <= 100; s += 1) {
				z = (double)s/10;
				double slice = Math.exp(-0.5 * z * z);
				logComputingEfficiencyUpperBoundMinimum = hinv(meanLogComputingEfficiencyNeeded + stdDevLogComputingEfficiencyNeeded * z, unboundedLogFlopsPerDollarForYear);
				numerator += slice * MathUtil.cumulativeNormalDistribution(-muX, sigmaX, -logComputingEfficiencyUpperBoundMinimum);
				denominator += slice;
			}
			org.singinst.uf.common.LogUtil.info(String.format("bound = %8g +/- %8g, minimum = %8g +/- %8g, unlimited = %8g", muX, sigmaX, meanLogComputingEfficiencyNeeded, stdDevLogComputingEfficiencyNeeded, unboundedLogFlopsPerDollarForYear));
		}
		org.singinst.uf.common.LogUtil.info(String.format("probabilityOfSufficientHardware called at year %5g, time %20.15g, result %20.16g", (double)year, ((double) System.currentTimeMillis()) / 1000, numerator / denominator));
		return numerator / denominator;
	}

	private double meanComputingEfficiencyNeeded() {
		// flops divided by dollars available
		return getDependency().value(NodeIDString.Q1_3, ScalarSubIDString.MEAN) - 
			getDependency().value(NodeIDString.Q1_4, ScalarSubIDString.MEAN);
	}

	private double stdDevComputingEfficiencyNeeded() {
		double stdDev1_3 = getDependency().value(NodeIDString.Q1_3, ScalarSubIDString.STD_DEV);
		double stdDev1_4 = getDependency().value(NodeIDString.Q1_4, ScalarSubIDString.STD_DEV);
		return Math.sqrt(
				stdDev1_3 * stdDev1_3 + stdDev1_4 * stdDev1_4);
	}

	/**
	 * sum conjugated with logarithm, conjugated with negation
	 * @param hypothetical log hardware production efficiency upper bound
	 * @param hypothetical log hardware production efficiency if no upper bound
	 * @return hypothetical log hardware production efficiency after effects of upper bound
	 */
	static double h(double x, double unboundedLogFlopsPerDollarForYear) {
		//return Math.log(exp(k) * exp(unboundedLogFlopsPerDollarForYear) / (exp(k) + exp(unboundedLogFlopsPerDollarForYear)));			
		if (x > unboundedLogFlopsPerDollarForYear) {
			return unboundedLogFlopsPerDollarForYear - Math.log1p(Math.exp(unboundedLogFlopsPerDollarForYear - x));
		} else {
			return x - Math.log1p(Math.exp(x - unboundedLogFlopsPerDollarForYear));
		}
	}
	
	/**
	 * 
	 * @param hypothetical log hardware production efficiency after effects of upper bound
	 * @param hypothetical log hardware production efficiency if no upper bound
	 * @return hypothetical log hardware production efficiency upper bound
	 */
	static double hinv(double h, double unboundedLogFlopsPerDollarForYear) {
		if (h >= unboundedLogFlopsPerDollarForYear) {
			return Double.POSITIVE_INFINITY;
		} else if ((h + Math.log(2)) >= unboundedLogFlopsPerDollarForYear) {
			return unboundedLogFlopsPerDollarForYear - Math.log(Math.expm1(unboundedLogFlopsPerDollarForYear-h));
		} else {
			return h - Math.log1p(-Math.exp(h-unboundedLogFlopsPerDollarForYear));
		}
	}

	private double muX() {
		return getDependency().value(
				NodeIDString.Q1_2, ScalarSubIDString.K50);
	}

	private double sigmaX() {
		return (muX() - getDependency().value(
				NodeIDString.Q1_2, ScalarSubIDString.K5)) / MathUtil.NINETY_FIVE_PERCENTILE;
	}

}
