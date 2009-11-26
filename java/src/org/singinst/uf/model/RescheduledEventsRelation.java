package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.singinst.uf.math.MathUtil;

public class RescheduledEventsRelation extends CalculationRelation {

	/*@Override
	protected Calculation getCalculationForYear(double year) {
		// TODO Auto-generated method stub
		return null;
	}*/

	public RescheduledEventsRelation(Node node) {
		super(node);
	}


	protected Calculation probabilityAiIsPossible() {
		return new Calculation("Probability that AGI is possible") {
			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				MathUtil.ScienceSpeedModelParameters scienceSpeedModelParameters = scienceSpeedModel();
	org.singinst.uf.common.LogUtil.info(String.format(
			  "institutional_base_year: %g\n"
			+ "institutional_mean_slope_log_factor: %g\n"
			+ "institutional_stddev_slope_log_factor: %g\n"
			+ "population_base_year: %g\n"
			+ "population_mean_log_year: %g\n"
			+ "population_stddev_log_year: %g\n"
			+ "population_mean_init_log_rate: %g\n"
			+ "population_stddev_init_log_rate: %g\n"
			+ "population_mean_slope_log_rate: %g\n"
			+ "population_stddev_slope_log_rate: %g\n"
			+ "sequencedata_base_year: %g\n"
			+ "sequencedata_mean_log_year: %g\n"
			+ "sequencedata_stddev_log_year: %g\n"
			+ "sequencedata_mean_init_log_factor: %g\n"
			+ "sequencedata_stddev_init_log_factor: %g\n"
			+ "sequencedata_mean_slope_log_factor: %g\n"
			+ "sequencedata_stddev_slope_log_factor: %g\n"
			, scienceSpeedModelParameters.institutional_base_year
			, scienceSpeedModelParameters.institutional_mean_slope_log_factor
			, scienceSpeedModelParameters.institutional_stddev_slope_log_factor
			, scienceSpeedModelParameters.population_base_year
			, scienceSpeedModelParameters.population_mean_log_year
			, scienceSpeedModelParameters.population_stddev_log_year
			, scienceSpeedModelParameters.population_mean_init_log_rate
			, scienceSpeedModelParameters.population_stddev_init_log_rate
			, scienceSpeedModelParameters.population_mean_slope_log_rate
			, scienceSpeedModelParameters.population_stddev_slope_log_rate
			, scienceSpeedModelParameters.sequencedata_base_year
			, scienceSpeedModelParameters.sequencedata_mean_log_year
			, scienceSpeedModelParameters.sequencedata_stddev_log_year
			, scienceSpeedModelParameters.sequencedata_mean_init_log_factor
			, scienceSpeedModelParameters.sequencedata_stddev_init_log_factor
			, scienceSpeedModelParameters.sequencedata_mean_slope_log_factor
			, scienceSpeedModelParameters.sequencedata_stddev_slope_log_factor
			));
	return getDependency().value(NodeIDString.Q1_1, ScalarSubIDString.PROBABILITY) / 100;
			}
		};
	}
	
	MathUtil.EventDiscreteDistributionSchedule rescheduledProbabilities;
	
	@Override
	protected List<Calculation> getCalculations(final List<Double> years) {
		List<Calculation> retVal = new ArrayList<Calculation>();
		for (final double year : years) {
			retVal.add(new Calculation("Probability of AI by " + (int) year) {

				@Override
				protected double rawEvaluate(StringBuilder htmlConsole) {
					if (year == ModelUtil.BASIC_MODEL_YEARS[0]) {
						calculate(MathUtil.wrapDoubleArray(ModelUtil.BASIC_MODEL_YEARS));
					}
					return MathUtil.expc(rescheduledProbabilities.logitnerp(year).clogProbEvent);
				}
			});
		}
		return retVal;
	}
	
	private void calculate(List<Double> years) {
		double[] yearsInOtherCalculations = ModelUtil.BASIC_MODEL_YEARS;
		double[] yearsHere = MathUtil.unwrapDoubleList(years);
		//new double[years.size()];
		//for (int i=0; i<yearsHere.length; ++i)
		//	yearsHere[i] = years.get(i);
		
		TreeSet<Double> beforeYearsSortedSet = new TreeSet<Double>(MathUtil.wrapDoubleArray(yearsInOtherCalculations));
		{
			double i = ModelUtil.LATEST_YEAR - ModelUtil.EARLIEST_IA_YEAR;
			double factor =
				  (yearsInOtherCalculations[yearsInOtherCalculations.length-1] - ModelUtil.EARLIEST_IA_YEAR)
				/ (yearsInOtherCalculations[yearsInOtherCalculations.length-2] - ModelUtil.EARLIEST_IA_YEAR)
				;
			i *= factor;
			while (i <= ModelUtil.LATEST_UNACCELERATED_YEAR - ModelUtil.EARLIEST_IA_YEAR) {
				beforeYearsSortedSet.add(Math.round(i/ModelUtil.YEAR_STEPSIZE)*ModelUtil.YEAR_STEPSIZE + ModelUtil.EARLIEST_IA_YEAR);
				i *= factor;
			};
			i = ModelUtil.LATEST_YEAR - ModelUtil.EARLIEST_IA_YEAR;
			i /= factor;
			while (i <= ModelUtil.EARLIEST_YEAR - ModelUtil.EARLIEST_IA_YEAR) {
				beforeYearsSortedSet.add(Math.round(i/ModelUtil.YEAR_STEPSIZE)*ModelUtil.YEAR_STEPSIZE + ModelUtil.EARLIEST_IA_YEAR);
				i /= factor;
			};
		}
		
		double[] beforeYears = MathUtil.unwrapDoubleList(new ArrayList<Double>(beforeYearsSortedSet));
		
		//List<Double> clogPAiWrapped; 
		
		double[] clogPAi;
		//clogPAi = MathUtil.unwrapDoubleList(clogPAiWrapped);
		clogPAi = new double[beforeYears.length];
		for (int i=0; i<clogPAi.length; ++i) {
		//	clogPAI[i] = 100;
			clogPAi[i] = MathUtil.clog(ScalarValueHolder.findById(NodeIDString.C1_6, ScalarSubIDString.yearProbabilityID(beforeYears[i])).getValue());
			if (clogPAi[i]>300d) clogPAi[i]=300d;
		}
		MathUtil.EventDiscreteDistributionSchedule s_before = new MathUtil.EventDiscreteDistributionSchedule();
		MathUtil.EventDiscreteDistributionSchedule s_after;

		s_before.time = beforeYears; //ModelUtil.BASIC_MODEL_YEARS;
		//s_before.time = java.util.Arrays.copyOf(ModelUtil.BASIC_MODEL_YEARS, yearsInOtherCalculations.length+2);
		//s_before.time[yearsInOtherCalculations.length] = ModelUtil.LATEST_YEAR*(1+2*MathUtil.DOUBLE_EPS);
		//s_before.time[yearsInOtherCalculations.length+1] = ModelUtil.LATEST_YEAR*(1+4*MathUtil.DOUBLE_EPS);
		s_before.clogProbEvent = clogPAi;
		//s_before.clogProbEvent = java.util.Arrays.copyOf(clogPAI, yearsInOtherCalculations.length+2);
		s_before.logitSubevents = new double[][]{};
		
		MathUtil.ScienceSpeedModelParameters scienceSpeedModelParameters = scienceSpeedModel();

		
		s_after = MathUtil.clogMarginalScienceSpeedEventRescheduling(
				scienceSpeedModelParameters,
				s_before, yearsHere, 1000);
		
		rescheduledProbabilities = s_after;
		
		//List<Double> rescheduledProbabilities = new java.util.ArrayList<Double>(years.size());
		//for (int i=0; i<rescheduledProbabilities.size(); ++i) {
		//	rescheduledProbabilities.set(i, MathUtil.expc(s_after.clogProbEvent[i]));
		//}
		
		/*				
		yearsInOtherCalculations = new double[ModelUtil.]*/

		//return rescheduledProbabilities;

	}

	// glue equations
	private MathUtil.ScienceSpeedModelParameters scienceSpeedModel() {
		MathUtil.ScienceSpeedModelParameters o = new MathUtil.ScienceSpeedModelParameters();

		o.sequencedata_base_year               = ModelUtil.ANCHOR_FAR_YEAR + ModelUtil.RESEARCH_CAREER_DELAY;
		o.sequencedata_mean_log_year           = getDependency().value(NodeIDString.Q1_9_1, ScalarSubIDString.MEAN)*Math.log(10);
		o.sequencedata_stddev_log_year         = getDependency().value(NodeIDString.Q1_9_1, ScalarSubIDString.STD_DEV)*Math.log(10);

		o.population_base_year                 = ModelUtil.ANCHOR_FAR_YEAR + ModelUtil.RESEARCH_CAREER_DELAY;
		o.population_mean_log_year             = getDependency().value(NodeIDString.Q1_9_2, ScalarSubIDString.MEAN)*Math.log(10);
		o.population_stddev_log_year           = getDependency().value(NodeIDString.Q1_9_2, ScalarSubIDString.STD_DEV)*Math.log(10);

		double t0, t1;
		t0 = 0; // EARLIEST_IA_DELAY
		t1 = ModelUtil.LATEST_IA_DELAY;
		double klow, khigh;
		klow  = NotablePercentile.PERCENTILE5.getValue();
		khigh = NotablePercentile.PERCENTILE95.getValue();
		double zlow, zhigh;
		zlow  = NotablePercentile.PERCENTILE5.getOffset();
		zhigh = NotablePercentile.PERCENTILE95.getOffset();
		double y0low, y0high, y1low, y1high;
		double mean_init, stddev_init, mean_slope, stddev_slope, mean_final, stddev_final;
		//y0low  = getDependency().value(NodeIDString.Q1_9_3, ScalarSubIDString.yearPercentileID(t0, klow ))*Math.log(10);
		//y0high = getDependency().value(NodeIDString.Q1_9_3, ScalarSubIDString.yearPercentileID(t0, khigh))*Math.log(10);
		y0low  = 0;
		y0high = 0;
		y1low  = getDependency().value(NodeIDString.Q1_9_3, ScalarSubIDString.yearPercentileID(t1, klow ))*Math.log(10);
		y1high = getDependency().value(NodeIDString.Q1_9_3, ScalarSubIDString.yearPercentileID(t1, khigh))*Math.log(10);
		mean_init = MathUtil.linterp(zlow, zhigh, y0low, y0high, 0);
		stddev_init = (y0high-y0low)/(zhigh-zlow);
		mean_final = MathUtil.linterp(zlow, zhigh, y1low, y1high, 0);
		stddev_final = (y1high-y1low)/(zhigh-zlow);
		mean_slope = (mean_final-mean_init)/(t1-t0);
		stddev_slope = (stddev_final-stddev_init)/(t1-t0);
		o.sequencedata_mean_init_log_factor     = mean_init;
		o.sequencedata_mean_slope_log_factor    = mean_slope;
		o.sequencedata_stddev_init_log_factor   = stddev_init;
		o.sequencedata_stddev_slope_log_factor  = stddev_slope;
		
		t0 = 0; // EARLIEST_IA_DELAY
		t1 = ModelUtil.LATEST_IA_DELAY;
		y0low  = getDependency().value(NodeIDString.Q1_9_4, ScalarSubIDString.yearPercentileID(t0, klow ))*Math.log(10);
		y0high = getDependency().value(NodeIDString.Q1_9_4, ScalarSubIDString.yearPercentileID(t0, khigh))*Math.log(10);
		y1low  = getDependency().value(NodeIDString.Q1_9_4, ScalarSubIDString.yearPercentileID(t1, klow ))*Math.log(10);
		y1high = getDependency().value(NodeIDString.Q1_9_4, ScalarSubIDString.yearPercentileID(t1, khigh))*Math.log(10);
		mean_init = MathUtil.linterp(zlow, zhigh, y0low, y0high, 0);
		stddev_init = (y0high-y0low)/(zhigh-zlow);
		mean_final = MathUtil.linterp(zlow, zhigh, y1low, y1high, 0);
		stddev_final = (y1high-y1low)/(zhigh-zlow);
		mean_slope = (mean_final-mean_init)/(t1-t0);
		stddev_slope = (stddev_final-stddev_init)/(t1-t0);
		o.population_mean_init_log_rate         = mean_init - Math.log(ModelUtil.UNENHANCED_RESEARCHERS);
		o.population_mean_slope_log_rate        = mean_slope - Math.log(ModelUtil.UNENHANCED_RESEARCHERS);
		o.population_stddev_init_log_rate       = stddev_init - Math.log(ModelUtil.UNENHANCED_RESEARCHERS);
		o.population_stddev_slope_log_rate      = stddev_slope - Math.log(ModelUtil.UNENHANCED_RESEARCHERS);
		
		t0 = ModelUtil.EARLIEST_YEAR;
		t1 = ModelUtil.LATEST_YEAR;
		y0low  = 0;
		y0high = 0;
		y1low  = getDependency().value(NodeIDString.Q1_9_5, ScalarSubIDString.yearPercentileID(t1, klow ))*Math.log(10);
		y1high = getDependency().value(NodeIDString.Q1_9_5, ScalarSubIDString.yearPercentileID(t1, khigh))*Math.log(10);
		mean_init = MathUtil.linterp(zlow, zhigh, y0low, y0high, 0);
		stddev_init = (y0high-y0low)/(zhigh-zlow);
		mean_final = MathUtil.linterp(zlow, zhigh, y1low, y1high, 0);
		stddev_final = (y1high-y1low)/(zhigh-zlow);
		mean_slope = (mean_final-mean_init)/(t1-t0);
		stddev_slope = (stddev_final-stddev_init)/(t1-t0);
		o.institutional_base_year               = ModelUtil.EARLIEST_YEAR;
		o.institutional_mean_slope_log_factor   = mean_slope;
		o.institutional_stddev_slope_log_factor = stddev_slope;
		
		return o;
	}

}
