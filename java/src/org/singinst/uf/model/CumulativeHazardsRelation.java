package org.singinst.uf.model;

//import java.util.List;

import org.singinst.uf.math.MathUtil;

public class CumulativeHazardsRelation extends YearwiseCalculationRelation {

	public CumulativeHazardsRelation(Node node) {
		super(node);
	}

	@Override
	protected Calculation getCalculation(double year) {
		return new AtLeastOneHappensCalculation("Probability of nuclear or other science-disrupting catastrophe by year " + (int) year, 
				probabilityOneCumulativeHazard(NodeIDString.Q1_7, "Independent probability of nuclear catastrophe by year ", year),
				probabilityOneCumulativeHazard(NodeIDString.Q1_8, "Independent probability of non-nuclear catastrophe by year ", year));

	}

	private Calculation probabilityOneCumulativeHazard(final String nodeId, final String description, final double year) {
		return new Calculation(description + (int) year) {

			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				double t0, t1;
				t0 = ModelUtil.EARLIEST_YEAR;
				t1 = ModelUtil.LATEST_YEAR;
				double klow, khigh;
				klow  = NotablePercentile.PERCENTILE5.getValue();
				khigh = NotablePercentile.PERCENTILE95.getValue();
				double zlow, zhigh;
				zlow  = NotablePercentile.PERCENTILE5.getOffset();
				zhigh = NotablePercentile.PERCENTILE95.getOffset();

				double y0low, y0high, y1low, y1high;
				y0low  = getDependency().value(nodeId, ScalarSubIDString.yearPercentileID(t0, klow ))*Math.log(10);
				y0high = getDependency().value(nodeId, ScalarSubIDString.yearPercentileID(t0, khigh))*Math.log(10);
				y1low  = getDependency().value(nodeId, ScalarSubIDString.yearPercentileID(t1, klow ))*Math.log(10);
				y1high = getDependency().value(nodeId, ScalarSubIDString.yearPercentileID(t1, khigh))*Math.log(10);

				double mean_init, stddev_init, mean_final, stddev_final;
				mean_init = MathUtil.linterp(zlow, zhigh, y0low, y0high, 0);
				stddev_init = (y0high-y0low)/(zhigh-zlow);
				mean_final = MathUtil.linterp(zlow, zhigh, y1low, y1high, 0);
				stddev_final = (y1high-y1low)/(zhigh-zlow);
				
				double[] clogp = MathUtil.clogMarginalOfExponentiallyVaryingHazardModel(
						t0, t1,
						mean_init, mean_final,
						stddev_init, stddev_final,
						new double[]{year}
				);				
				
				return MathUtil.expc(clogp[0]);
			}
			
		};
	}	

}
