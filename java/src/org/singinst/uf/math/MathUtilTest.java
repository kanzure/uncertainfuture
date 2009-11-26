package org.singinst.uf.math;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.math.analysis.RombergIntegrator;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class MathUtilTest extends TestCase {

	static MathUtil.EventDiscreteDistributionSchedule heavisideSchedule(double t) {
		MathUtil.EventDiscreteDistributionSchedule o = new MathUtil.EventDiscreteDistributionSchedule();
		double[] o_dot_time = {0, t*(1-MathUtil.DOUBLE_EPS), t, 1e100};
		o.time = o_dot_time;
		double[] o_dot_clogProbEvent = {0,0,10,10};
		o.clogProbEvent = o_dot_clogProbEvent;
		o.logitSubevents = new double[0][];
		return o;
	}
	
	static MathUtil.ScienceSpeedModelParameters nullScienceModel() {
		MathUtil.ScienceSpeedModelParameters o = new MathUtil.ScienceSpeedModelParameters();
	 	o.sequencedata_base_year = 0;
	 	o.sequencedata_mean_log_year = 0;
	 	o.sequencedata_stddev_log_year = 0;
	 	o.sequencedata_mean_init_log_factor = 0;
	 	o.sequencedata_stddev_init_log_factor = 0;
	 	o.sequencedata_mean_slope_log_factor = 0;
	 	o.sequencedata_stddev_slope_log_factor = 0;
	 	o.population_base_year = 0;
	 	o.population_mean_log_year = 0;
	 	o.population_stddev_log_year = 0;
	 	o.population_mean_init_log_rate = 0;
	 	o.population_stddev_init_log_rate = 0;
	 	o.population_mean_slope_log_rate = 0;
	 	o.population_stddev_slope_log_rate = 0;
	 	o.institutional_base_year = 0;
	 	o.institutional_mean_slope_log_factor = 0;
	 	o.institutional_stddev_slope_log_factor = 0;
	 	return o;
	}

	static MathUtil.ScienceSpeedZValues nullScienceSpeedZValues () {
		MathUtil.ScienceSpeedZValues o = new MathUtil.ScienceSpeedZValues();
		return o;
	}
	
	static MathUtil.ScienceSpeedModelParameters pointMassScienceSpeedModel(MathUtil.ScienceSpeedModelParameters m, MathUtil.ScienceSpeedScenario s) {
		MathUtil.ScienceSpeedModelParameters o = nullScienceModel();
		o.institutional_mean_slope_log_factor = s.institutional_slope_log_factor;
		if (s.population_year > m.population_base_year) {
			o.population_mean_log_year = Math.log(s.population_year - m.population_base_year);
			o.population_base_year = m.population_base_year;
		} else {
			o.population_mean_log_year = Double.NEGATIVE_INFINITY;
			o.population_base_year = s.population_year;
		}
		o.population_mean_init_log_rate = s.population_init_log_rate;
		o.population_mean_slope_log_rate = s.population_slope_log_rate;
		if (s.sequencedata_year > m.sequencedata_base_year) {
			o.sequencedata_mean_log_year = Math.log(s.sequencedata_year - m.sequencedata_base_year);
			o.sequencedata_base_year = m.sequencedata_base_year;
		} else {
			o.sequencedata_mean_log_year = Double.NEGATIVE_INFINITY;
			o.sequencedata_base_year = s.sequencedata_year;
		}
		o.sequencedata_mean_init_log_factor = s.sequencedata_init_log_factor;
		o.sequencedata_mean_slope_log_factor = s.sequencedata_slope_log_factor;
		return o;
	}

	static MathUtil.ScienceSpeedScenario applyScienceSpeedZValues (MathUtil.ScienceSpeedModelParameters m, MathUtil.ScienceSpeedZValues z) {
		MathUtil.ScienceSpeedScenario o = new MathUtil.ScienceSpeedScenario();
		o.institutional_slope_log_factor = 
			  m.institutional_mean_slope_log_factor
			+ z.institutional_rate_Z*m.institutional_stddev_slope_log_factor;
		o.population_year =
			  m.population_base_year
			+ Math.exp(
				  m.population_mean_log_year
				+ z.population_year_Z*m.population_stddev_log_year);
		o.sequencedata_year =
			  m.sequencedata_base_year
			+ Math.exp(
				  m.sequencedata_mean_log_year
				+ z.sequencedata_year_Z*m.sequencedata_stddev_log_year);
		o.population_init_log_rate = 
			  m.population_mean_init_log_rate
			+ z.population_rate_Z*m.population_stddev_init_log_rate;
		o.population_slope_log_rate = 
			  m.population_mean_slope_log_rate
			+ z.population_rate_Z*m.population_stddev_slope_log_rate;
		o.sequencedata_init_log_factor = 
			+ m.sequencedata_mean_init_log_factor
			+ z.sequencedata_factor_Z*m.sequencedata_stddev_init_log_factor;
		o.sequencedata_slope_log_factor =
			+ m.sequencedata_mean_slope_log_factor
			+ z.sequencedata_factor_Z*m.sequencedata_stddev_slope_log_factor;
		return o;
	}

	static MathUtil.ScienceSpeedScenarioReduced applyScienceSpeedZValuesReduce (MathUtil.ScienceSpeedModelParameters m, MathUtil.ScienceSpeedZValues z) {
		MathUtil.ScienceSpeedScenarioReduced o = new MathUtil.ScienceSpeedScenarioReduced();
		o.institutional_slope_log_factor = 
			  m.institutional_mean_slope_log_factor
			+ z.institutional_rate_Z*m.institutional_stddev_slope_log_factor;
		o.sciencetalent_rel_year =
			  m.population_base_year
			+ Math.exp(
				  m.population_mean_log_year
				+ z.population_year_Z*m.population_stddev_log_year)
			- m.institutional_base_year;
		double sequencedata_rel_year =
			  m.sequencedata_base_year
			+ Math.exp(
				  m.sequencedata_mean_log_year
				+ z.sequencedata_year_Z*m.sequencedata_stddev_log_year)
			- m.institutional_base_year;
		o.sciencetalent_init_log_rate = 
			  m.population_mean_init_log_rate
			+ z.population_rate_Z*m.population_stddev_init_log_rate
			+ m.sequencedata_mean_init_log_factor
			+ (o.sciencetalent_rel_year - sequencedata_rel_year)
			  * (m.sequencedata_mean_slope_log_factor)
			+ z.sequencedata_factor_Z * (
			    m.sequencedata_stddev_init_log_factor
			  + (o.sciencetalent_rel_year - sequencedata_rel_year)
			    * (m.sequencedata_stddev_slope_log_factor)
			  );
		o.sciencetalent_slope_log_rate =
			  m.population_mean_slope_log_rate
			+ z.population_rate_Z*m.population_stddev_slope_log_rate
			+ m.sequencedata_mean_slope_log_factor
			+ z.sequencedata_factor_Z*m.sequencedata_stddev_slope_log_factor;
		return o;
	}
	
	static void logDoubleList(double[] l) {
		StringBuilder b = new StringBuilder();
		for (int i=0; i<l.length; ++i) {
			b.append(String.format("%20.16g ", l[i]));
		}		
		org.singinst.uf.common.LogUtil.info(b.toString());
	}

	static void logDoubleList(String s, double[] l) {
		StringBuilder b = new StringBuilder(s);
		for (int i=0; i<l.length; ++i) {
			b.append(String.format("%20.16g ", l[i]));
		}		
		org.singinst.uf.common.LogUtil.info(b.toString());
	}
	
	static double[] projectScienceYearsNumerical(MathUtil.ScienceSpeedScenario s, double[] t) throws org.apache.commons.math.FunctionEvaluationException,org.apache.commons.math.ConvergenceException {
		double[] o = new double[t.length];
		final double t0_seq, m_seq, b_seq;
		final double t0_pop, m_pop, b_pop;
		final double t0_inst, m_inst;
		
		t0_seq = s.sequencedata_year;
		m_seq = s.sequencedata_slope_log_factor;
		b_seq = s.sequencedata_init_log_factor;
		t0_pop = s.population_year;
		m_pop = s.population_slope_log_rate;
		b_pop = s.population_init_log_rate;
		t0_inst = s.institutional_base_year;
		m_inst = s.institutional_slope_log_factor;
		
		UnivariateRealFunction science_rate_after_pop = new UnivariateRealFunction() {
			public double value(double t) {
				return (t-t0_pop)*MathUtil.exprel((m_seq+m_pop)*(t-t0_pop))*Math.exp(m_inst*(t-t0_inst)+b_pop+m_seq*(t0_pop-t0_seq)+b_seq);
				//return (t-t0_pop)*MathUtil.exprel(m_pop*(t-t0_pop))*Math.exp(b_pop+m_inst*(t-t0_inst)+b_seq+m_seq*(t-t0_seq));
			}
		};
		double baseyear = s.institutional_base_year;
		double prevyear = t0_pop; double science_from_pop = 0;
		double year; double science;
		org.apache.commons.math.analysis.UnivariateRealIntegrator science_from_pop_integrator = new RombergIntegrator(science_rate_after_pop);
		science_from_pop_integrator.setRelativeAccuracy(32*MathUtil.DOUBLE_EPS);
		for (int i=0; i<t.length; ++i) {
			year = t[i];
			science = (year-baseyear)*MathUtil.exprel(m_inst*(year-baseyear));
			if (year >= t0_pop) {
				if (year > prevyear) {
					science_from_pop = science_from_pop + science_from_pop_integrator.integrate(prevyear, year);
					prevyear = year;
				} else if (year < prevyear) {
					science_from_pop = science_from_pop - science_from_pop_integrator.integrate(year, prevyear);
					prevyear = year;					
				}
				science = science + science_from_pop;
			}
			o[i] = science + baseyear;
		}
		return o;
	}
	
	static double[] projectScienceYearsAnalytic(MathUtil.ScienceSpeedScenario s, double[] rescheduled_years) {
		double[] o = new double[rescheduled_years.length];
		double dt0_seq, m_seq, b_seq;
		double dt0_pop, m_pop, b_pop;
		double t0_inst, m_inst;
		
		double dt, science_years;
		
		t0_inst = s.institutional_base_year;
		m_inst = s.institutional_slope_log_factor;
		dt0_seq = s.sequencedata_year - t0_inst;
		m_seq = s.sequencedata_slope_log_factor;
		b_seq = s.sequencedata_init_log_factor;
		dt0_pop = s.population_year - t0_inst;
		m_pop = s.population_slope_log_rate;
		b_pop = s.population_init_log_rate;
		
		for (int j=0; j<rescheduled_years.length; ++j) {
			dt = rescheduled_years[j] - t0_inst;
			science_years = MathUtil.exprel( m_inst * dt ) * dt;
			if (dt > dt0_pop)
				science_years += (dt-dt0_pop)*(dt-dt0_pop) * Math.exp( m_inst*((1*dt0_pop+2*dt)/3/* - 0 */) + m_seq*((2*dt0_pop+1*dt)/3-dt0_seq)+b_seq + m_pop*((2*dt0_pop+1*dt)/3-dt0_pop)+b_pop ) * MathUtil.rpn_b_expm1_a_t_a_expm1_b_t_m_a_b_p_3_d_exp_d_a_d_b_d_a_b_m_d( -m_inst*(dt-dt0_pop), (m_pop+m_seq)*(dt-dt0_pop) ) ;
			o[j] = science_years + t0_inst;
		}
		return o;
	}

	static double[] projectScienceYearsAnalytic(MathUtil.ScienceSpeedScenarioReduced s, double[] reschedule_rel_years, double base_year) {
		double[] o = new double[reschedule_rel_years.length];
		double dt0_tal, m_tal, b_tal;
		double m_inst;
		
		double dt, science_years;
		
		m_inst = s.institutional_slope_log_factor;
		dt0_tal = s.sciencetalent_rel_year;
		m_tal = s.sciencetalent_slope_log_rate;
		b_tal = s.sciencetalent_init_log_rate;
		
		for (int j=0; j<reschedule_rel_years.length; ++j) {
			dt = reschedule_rel_years[j] - base_year;
			science_years = MathUtil.exprel( m_inst * dt ) * dt;
			if (dt > dt0_tal)
				science_years += (dt-dt0_tal)*(dt-dt0_tal) * Math.exp( m_inst*((1*dt0_tal+2*dt)/3/* - 0 */) + m_tal*((2*dt0_tal+1*dt)/3-dt0_tal)+b_tal ) * MathUtil.rpn_b_expm1_a_t_a_expm1_b_t_m_a_b_p_3_d_exp_d_a_d_b_d_a_b_m_d( -m_inst*(dt-dt0_tal), m_tal*(dt-dt0_tal) ) ;
			o[j] = science_years + base_year;
		}
		return o;
	}

	static public void testShowMeWhatClogIntervalLooksLike() throws Exception {
		double d = MathUtil.clogIntervalAverage(0, 4);
		org.singinst.uf.common.LogUtil.info(new Double(d).toString());
	}
	
	void notclearonthis() throws Exception {
		for (int i=0; i<2; ++i) {
		final double d = MathUtil.RANDOM.nextDouble();
		UnivariateRealFunction f = new UnivariateRealFunction() {
			public double value(double x) {
				return d*x;
			}
		};
		RombergIntegrator ri = new RombergIntegrator(f);

		org.singinst.uf.common.LogUtil.info(String.format("%g %g", d/2, ri.integrate(0, 1)));
		}
	}
	
	public void testMeh() throws Exception {
		{

		notclearonthis();
		notclearonthis();
			
		MathUtil.EventDiscreteDistributionSchedule s = new MathUtil.EventDiscreteDistributionSchedule();
		double[] s_dot_time = {0, 1, 2, 3, 4, 4.1, 4.2, 4.3, 4.4};
		double[] t = {-0.5,0,0.5,1,1.5,2,2.5,3,3.5,4,4.05,4.1,4.15,4.2,4.25,4.3,4.35,4.4,4.45};
		
		s.time = s_dot_time;
		double[] s_dot_clogProbEvent = {0,1,2,3,4,5,6,7,8};
		s.clogProbEvent = s_dot_clogProbEvent;
		s.logitSubevents = new double[0][];
		MathUtil.EventDiscreteDistributionSchedule r = s.logitnerp(t);
		StringBuilder b = new StringBuilder();
		for (int i=0; i<r.clogProbEvent.length; ++i) {
			b.append(r.clogProbEvent[i]).append(" ");
		}
		org.singinst.uf.common.LogUtil.info(b.toString());
		}
		
		{
		MathUtil.EventDiscreteDistributionSchedule s = heavisideSchedule(1);
		double[] t = {0,0.5,1,1.5,2};
		MathUtil.EventDiscreteDistributionSchedule r = s.logitnerp(t);
		logDoubleList(r.clogProbEvent);
		}
	}	

	static double LOG_DOUBLE_EPS = Math.log(MathUtil.DOUBLE_EPS);
	static double square_clogged(double clog_p) {
		if (clog_p >= -LOG_DOUBLE_EPS) return clog_p - MathUtil.LOG_TWO;
		if (clog_p < LOG_DOUBLE_EPS) return 2*clog_p;
		return -Math.log(Math.exp(-clog_p)*(2-Math.exp(-clog_p)));
	}
	static double[] square_clogged(double[] clog_p) {
		double[] o = new double[clog_p.length];
		for (int i=0; i<clog_p.length; ++i) {
			if (clog_p[i] >= -LOG_DOUBLE_EPS) o[i] = clog_p[i] - MathUtil.LOG_TWO;
			if (clog_p[i] < LOG_DOUBLE_EPS) o[i] = 2*clog_p[i];
			o[i] = -Math.log(Math.exp(-clog_p[i])*(2-Math.exp(-clog_p[i])));			
		}
		return o;
	}
	
	static double PHI = 0.618033988749894848d;
	static double COS_TWO_THIRDS_PI_PHI =  0.272883452047768708d;
	static double SIN_TWO_THIRDS_PI_PHI =  0.962047099469923622d;
	
	interface ScenarioSource { MathUtil.ScienceSpeedScenarioReduced scenario(int i); };
	
	public void testWhatever() throws Exception {
		int iters = 1000;
		MathUtil.ScienceSpeedModelParameters m = nullScienceModel();
		MathUtil.ScienceSpeedScenario sss = new MathUtil.ScienceSpeedScenario();
		MathUtil.ScienceSpeedScenarioReduced sssr = new MathUtil.ScienceSpeedScenarioReduced();
		MathUtil.ScienceSpeedZValues z = new MathUtil.ScienceSpeedZValues();
		
		sss.institutional_base_year = -MathUtil.RANDOM.nextDouble();
		//sss.institutional_base_year = -0.5;
		sss.institutional_slope_log_factor = MathUtil.RANDOM.nextGaussian();
		//sss.institutional_slope_log_factor = Math.log(1.0001)*10;
		sss.sequencedata_init_log_factor = MathUtil.RANDOM.nextGaussian();
		sss.sequencedata_slope_log_factor = MathUtil.RANDOM.nextGaussian();
		//sss.sequencedata_slope_log_factor = Math.log(1000);
		sss.sequencedata_year = MathUtil.RANDOM.nextDouble();
		sss.population_init_log_rate = MathUtil.RANDOM.nextGaussian();
		// //sss.population_init_log_rate = Math.log(10000);
		sss.population_slope_log_rate = MathUtil.RANDOM.nextGaussian();
		sss.population_year = MathUtil.RANDOM.nextDouble();
		
		MathUtil.EventDiscreteDistributionSchedule s = heavisideSchedule(1);
		m.institutional_base_year = sss.institutional_base_year;
		m.population_base_year = 0;
		m.population_mean_log_year = 0;
		m.population_stddev_log_year = 1;
		m.sequencedata_base_year = 0;
		m.sequencedata_mean_log_year = 0;
		m.sequencedata_stddev_log_year = 1;
		m = pointMassScienceSpeedModel(m, sss);
		//m.institutional_base_year = sss.institutional_base_year;
		sss = applyScienceSpeedZValues(m,z);
		sssr = applyScienceSpeedZValuesReduce(m,z);
		double[] t = {0.0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0,1.1,1.2,1.3,1.4,1.5,2.0};
		double[] clog_weird
		           = {0  ,0  ,1  ,1  ,2  ,2  ,3  ,3  ,4  ,4  ,5  ,5  ,6  ,6  ,7  ,7  ,8  };
		
		logDoubleList(t);
		logDoubleList(projectScienceYearsNumerical(sss,t));
		logDoubleList(projectScienceYearsAnalytic(sss,t));
		logDoubleList(projectScienceYearsAnalytic(sssr,t,m.institutional_base_year));
		s.time = t;
		s.clogProbEvent = t;
		//s.clogProbEvent = clog_weird;
		MathUtil.EventDiscreteDistributionSchedule r = MathUtil.clogMarginalScienceSpeedEventRescheduling(m, s, t, iters);
		logDoubleList(r.clogProbEvent);
		
		r = s;
		MathUtil.EventDiscreteDistributionSchedule r_regular = s;
		MathUtil.EventDiscreteDistributionSchedule r_montecarlo = s;
		MathUtil.ScienceSpeedModelParameters m_v = m.clone();

		double wt = 0, wt_i;
		double[] squared_clog = new double[t.length];
		
		m_v.institutional_stddev_slope_log_factor = 1;
		for (int i=0; i<=200; ++i) {
			z.institutional_rate_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.institutional_rate_Z*z.institutional_rate_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year));
		logDoubleList(s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)).clogProbEvent);
		logDoubleList("inst rate Z by regular", r_regular.clogProbEvent);
		logDoubleList("inst rate Z by regular", r_montecarlo.clogProbEvent);
		m_v.institutional_stddev_slope_log_factor = 0;
		z.institutional_rate_Z = 0;
		
		m_v.population_stddev_init_log_rate = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.population_rate_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.population_rate_Z*z.population_rate_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("pop init rate Z by regular", r_regular.clogProbEvent);
		logDoubleList("pop init rate Z by MC     ", r_montecarlo.clogProbEvent);
		m_v.population_stddev_init_log_rate = 0;
		z.population_rate_Z = 0;
		
		m_v.population_stddev_slope_log_rate = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.population_rate_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.population_rate_Z*z.population_rate_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("pop slope rate Z by regular", r_regular.clogProbEvent);
		logDoubleList("pop slope rate Z by MC     ", r_montecarlo.clogProbEvent);
		m_v.population_stddev_slope_log_rate = 0;
		z.population_rate_Z = 0;

		m_v.population_stddev_log_year = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.population_year_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.population_year_Z*z.population_year_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		logDoubleList("pop year Z by regular Z", r_regular.clogProbEvent);
		for (int i=0; i<iters; ++i) {
			z.population_year_Z = MathUtil.inverseCumulativeProbability(0, 1, (i+0.5d)/iters);
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = 1;
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
			//org.singinst.uf.common.LogUtil.info(String.format("regular %g leads to progress %g", sssr.sciencetalent_rel_year, rescheduled_t[rescheduled_t.length-1]));
		}
		logDoubleList("pop year Z by regular q", r_regular.clogProbEvent);
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("pop year Z by MC       ", r_montecarlo.clogProbEvent);
		m_v.population_stddev_log_year = 0;
		z.population_year_Z = 0;
		
		m_v.sequencedata_stddev_init_log_factor = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.sequencedata_factor_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.sequencedata_factor_Z*z.sequencedata_factor_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("seqdata init factor Z by regular", r_regular.clogProbEvent);
		logDoubleList("seqdata init factor Z by MC     ", r_montecarlo.clogProbEvent);
		m_v.sequencedata_stddev_init_log_factor = 0;
		z.sequencedata_factor_Z = 0;
		
		m_v.sequencedata_stddev_slope_log_factor = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.sequencedata_factor_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.sequencedata_factor_Z*z.sequencedata_factor_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("seqdata slope factor Z by regular", r_regular.clogProbEvent);
		logDoubleList("seqdata slope factor Z by MC     ", r_montecarlo.clogProbEvent);
		m_v.sequencedata_stddev_slope_log_factor = 0;
		z.sequencedata_factor_Z = 0;

		m_v.sequencedata_stddev_log_year = 1;
		wt = 0;
		for (int i=0; i<=200; ++i) {
			z.sequencedata_year_Z = (i-100)/10d;
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = Math.exp(-z.sequencedata_year_Z*z.sequencedata_year_Z/2);
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
		}
		r_montecarlo = MathUtil.clogMarginalScienceSpeedEventRescheduling(m_v, s, t, iters);
		logDoubleList("seqdata year Z by regular Z", r_regular.clogProbEvent);
		for (int i=0; i<iters; ++i) {
			z.sequencedata_year_Z = MathUtil.inverseCumulativeProbability(0, 1, (i+0.5d)/iters);
			sssr = applyScienceSpeedZValuesReduce(m_v,z);
			wt_i = 1;
			double[] rescheduled_t = projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year);
			MathUtil.EventDiscreteDistributionSchedule s_i = s.logitnerp(rescheduled_t);
			r_regular = MathUtil.EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s_i);
			//r_regular = EventDiscreteDistributionSchedule.weightedMix(wt, wt_i, r_regular, s.logitnerp(projectScienceYearsAnalytic(sssr,t,m_v.institutional_base_year)));
			wt = wt + wt_i;
			//org.singinst.uf.common.LogUtil.info(String.format("regular %g leads to progress %g", sssr.sciencetalent_rel_year, rescheduled_t[rescheduled_t.length-1]));
		}
		logDoubleList("seqdata year Z by regular q", r_regular.clogProbEvent);
		logDoubleList("seqdata year Z by MC       ", r_montecarlo.clogProbEvent);
		m_v.sequencedata_stddev_log_year = 0;
		z.sequencedata_year_Z = 0;
		
		
		fail();
	}
	
	static double SQRT_EPS = Math.sqrt(MathUtil.DOUBLE_EPS);
	public double u(double x) {
		return x+SQRT_EPS*(Math.abs(x)+MathUtil.DOUBLE_MIN_NORMAL);
	}
	public double d(double x) {
		return x-SQRT_EPS*(Math.abs(x)/*+MathUtil.DOUBLE_MIN_NORMAL*/);
	}
	
	public void testWeightedClogMix() throws Exception {
		int i,j,k,l;
		double o, cl_p;
		for (i=0; i<6; ++i) for (j=0; j<6; ++j) for (k=0; k<6; ++k) for (l=0; l<6; ++l) {
			o = MathUtil.weightedClogMix(i, j, k, l);
			cl_p = MathUtil.clog((i*MathUtil.expc(k)+j*MathUtil.expc(l))/(i+j));
			Assert.assertEquals(cl_p, o, 1e-14d*cl_p);
			//LogUtil.info(String.format("%d %d %d %d %20.14g %20.14g %20.14g %20.14g", i,j,k,l, o, p, o-p, Math.exp(-((o-p)%100)) ));
		}
		double a,b,c,d;
		double w0, w1, clog_p0, clog_p1;
		double cl_p_hi, cl_p_lo;
		for (a=-50; a<=50; a+=25) for (b=-50; b<50; b+=25) for (c=0; c<=100; c+=25) for (d=0; d<=100; d+=25) {
			w0 = Math.exp(a); clog_p0 = c;
			w1 = Math.exp(b); clog_p1 = d;
			o = MathUtil.weightedClogMix(w0, w1, clog_p0, clog_p1);
//			cl_p = clog_p0-Math.log1p((w1*Math.expm1(clog_p0-clog_p1))/(w0+w1));
//			Assert.assertEquals(cl_p, o, 16*MathUtil.DOUBLE_EPS*(
//					  Math.abs( clog_p0 )
//					+ Math.abs( Math.log1p((w1*Math.expm1(clog_p0-clog_p1))/(w0+w1)) )
//					+ Math.abs( 1/(1+(w1*Math.expm1(clog_p0-clog_p1))/(w0+w1)) )
//				) );
			cl_p = clog_p0-Math.log1p((Math.expm1(clog_p0-clog_p1))/(w0/w1+1));
			cl_p_hi = u(u(clog_p0)-d(Math.log1p(Math.max(1,d(d(Math.expm1(d(d(clog_p0)-u(clog_p1))))/(w0/w1+1))))));
			cl_p_lo = d(d(clog_p0)-u(Math.log1p(           u(u(Math.expm1(u(u(clog_p0)-d(clog_p1))))/(w0/w1+1)) )));
			try {
				Assert.assertEquals(cl_p, o, 16*SQRT_EPS*(Math.abs(cl_p_hi-cl_p)+Math.abs(cl_p_lo-cl_p)));
			} catch (junit.framework.AssertionFailedError e) {
				o = MathUtil.weightedClogMix(w0, w1, clog_p0, clog_p1);
				throw(e);
			}
			
//			cl_p = clog_p1-Math.log1p((w0*Math.expm1(clog_p1-clog_p0))/(w0+w1));
//			Assert.assertEquals(cl_p, o, 16*MathUtil.DOUBLE_EPS*(
//					  Math.abs( clog_p1 )
//					+ Math.abs( Math.log1p((w0*Math.expm1(clog_p1-clog_p0))/(w0+w1)) )
//					+ Math.abs( 1/(1+(w0*Math.expm1(clog_p1-clog_p0))/(w0+w1)) )
//				) );
			cl_p = clog_p1-Math.log1p((Math.expm1(clog_p1-clog_p0))/(w1/w0+1));
			cl_p_hi = u(u(clog_p1)-d(Math.log1p(Math.max(1,d(d(Math.expm1(d(d(clog_p1)-u(clog_p0))))/(w1/w0+1))))));
			cl_p_lo = d(d(clog_p1)-u(Math.log1p(           u(u(Math.expm1(u(u(clog_p1)-d(clog_p0))))/(w1/w0+1)) )));
			try {
				Assert.assertEquals(cl_p, o, 16*SQRT_EPS*(Math.abs(cl_p_hi-cl_p)+Math.abs(cl_p_lo-cl_p)));
			} catch (junit.framework.AssertionFailedError e) {
				o = MathUtil.weightedClogMix(w0, w1, clog_p0, clog_p1);
				throw(e);
			}

		}
	}
	
	public void test() throws Exception {
	}
}
