package org.singinst.uf.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.special.Erf;

public class MathUtil {

	public static double[] unwrapDoubleList(java.util.List<Double> l) {
		double[] retVal = new double[l.size()];
		for (int i=0; i<retVal.length; ++i) retVal[i] = l.get(i);
		return retVal;
	}
	public static java.util.ArrayList<Double> wrapDoubleArray(double[] a) {
		java.util.ArrayList<Double> retVal = new java.util.ArrayList<Double>(a.length);
		for (int i=0; i<a.length; ++i) retVal.add(a[i]);
		return retVal;
	}
	
	public static double clog(double p) {
		return -Math.log1p(-p);
	} 
	public static double expc(double q) {
		return -Math.expm1(-q);
	}
	
	public static double logit(double p) {
		if (p<0 || p>1) { return Double.NaN; }
		if (p==0) { return Double.NEGATIVE_INFINITY; }
		if (p==1) { return Double.POSITIVE_INFINITY; }
		// if (p>0.25) { return Math.atanh(p*2-1)*2;...? oh }
		return Math.log(p/(1-p));
	}
	public static double logistic(double t) {
		return 1/(1+Math.exp(-t));
	}

	public static double log_logistic(double t) {
		//return Math.log(logistic(t));
		if (t <= -38) {
			return t;
		} else if (t > 38) {
			return -Math.exp(-t);
		}
		if (t<0) {
			return t - Math.log1p(Math.exp(t)); 
		} else {
			double r = Math.exp(-t);
			return Math.log1p(-r/(1+r));
		}
	}
	public static double logit_exp(double b) {
		//return logit(Math.exp(b));
		return b - Math.log(-Math.expm1(b));
	}
	public static double clog_logistic(double t) {
		return -log_logistic(-t);
	}
	public static double logit_expc(double q) {
		//return logit(expc(q));
		return q + Math.log(-Math.expm1(-q));
	}
	
	       public static final double DOUBLE_EPS = 1-(1-(double)1.2e-16);
	       static final double SQRT_ONE_OVER_EIGHT_PI = Math.sqrt(1/(8*Math.PI));
	       static final double SQRT_ONE_HALF = Math.sqrt(0.5);
	public static double cumulativeNormalDistribution(double mean,
			double stdDev, double x) {
		try {
			double Z = (x - mean)/stdDev;
			if (Z<=-38.5) {
				return 0;
			} else if (Z>8.3) {
				return 1;
			} else if (Z<-1.15) {
				// absTol = DOUBLE_EPS/-Z because lg2(Z) mantissa bits in Z are lost as exponent bits in exp(-Z*Z/2)
				return SQRT_ONE_OVER_EIGHT_PI * normalTailCF(Z*Z/2, DOUBLE_EPS/-Z) * (-Z) * Math.exp(-Z*Z/2);
			} else if (Z>2) {
				double absTol = Math.exp(Z*Z/2)*DOUBLE_EPS/Z;
				return 1 - SQRT_ONE_OVER_EIGHT_PI * normalTailCF(Z*Z/2, absTol) * (Z) * Math.exp(-Z*Z/2);
			} else {
				// jakarta commons implementation is acceptable
				return 0.5 * (1+Erf.erf(Z*SQRT_ONE_HALF));
			}
		} catch (MathException e) {
			throw new RuntimeException(e);
		}
	}

	public static double logitCumulativeNormalDistribution(double mean,
			double stdDev, double x) {
		try {
			double Z = (x - mean)/stdDev;
			double aZ = Math.abs(Z);
			if (aZ >= 1e9) {
				return Math.signum(Z)/2*Z*Z;
			} else if (aZ >= 2e4) {
				return Math.signum(Z)*(Z*Z/2-Math.log(SQRT_ONE_OVER_EIGHT_PI * 2/aZ));
			} else if (aZ >= 1.15) {
				double clogp = Z*Z/2-Math.log(SQRT_ONE_OVER_EIGHT_PI * normalTailCF(Z*Z/2, DOUBLE_EPS/aZ) * aZ);
				return Math.signum(Z)*(clogp+Math.log1p(-Math.exp(-clogp)));
			} else {
				double e = Erf.erf(Z*SQRT_ONE_HALF);
				return Math.log1p(e)-Math.log1p(-e);
			}
		} catch (MathException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static double linterp(double x0, double x1, double y0, double y1, double x) {
		if (x0==x1) { return (y0+y1)/2; }
		if (Math.abs(x1)>Math.abs(x0)) {
			if (Math.abs(x) <= Math.abs(x0)) {
				double b = (x0/(x0-x1)) * (y1-y0) + y0;
				return (x/x1) * (y1-b) + b;
			} else {
				return ((x-x0)/(x1-x0)) * (y1-y0) + y0;
			}
		} else {
			if (Math.abs(x) <= Math.abs(x1)) {
				double b = (x1/(x1-x0)) * (y0-y1) + y1;
				return (x/x0) * (y0-b) + b;
			} else {
				return ((x-x1)/(x0-x1)) * (y0-y1) + y1;
			}
		}
	}
	
	public static double linterp(double[] x, double[] y, double nx) {
		if (y.length <= 1) { return y[0]; } // ArrayIndexOutOfBoundsException if y.length==0
		if (nx == Double.POSITIVE_INFINITY) {
			double yu = y[y.length-1], ypu = y[y.length-2];
			if (yu>ypu) {
				return Double.POSITIVE_INFINITY;
			} else if (yu<ypu) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return yu;
			}
		}
		if (nx == Double.NEGATIVE_INFINITY) {
			double yu = y[0], ypu = y[1];
			if (yu>ypu) {
				return Double.POSITIVE_INFINITY;
			} else if (yu<ypu) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return yu;
			}
		}		
		int n = java.util.Arrays.binarySearch(x, nx);
		if (n >= 0) { return y[n]; }
		else if (n == -1) { n = -2; }
		else if (n == -x.length-1) { n = -x.length; }
		n = -n-1;
		return linterp(x[n-1], x[n], y[n-1], y[n], nx);
	}
	
	public static double[] linterp(double[] x, double[] y, double[] nx) {
		double[] ny = new double[nx.length];
		for (int i=0; i<nx.length; ++i) {
			ny[i] = linterp(x, y, nx[i]);
		}
		return ny;
	}

	public static double normalTailCF(double t, double absTol) {
		/* Disclaimer: I am not a numerical analyst */
		double p,q,r,s,pn,qn,rn,sn,b,f,fn;
		b = 0;
		pn = 0; qn = 1; rn = 1; sn = t;
		fn = 1/t;
		for (int k=1; k<400; k=k+2) {
			p = pn; q = qn; r = rn; s = sn; f = fn;
			/* continued fraction */
			pn = (k)*p + (q+q);
			qn = (k)*t*p + ((k+1)+(t+t))*q;
			rn = (k)*r + (s+s);
			sn = (k)*t*r + ((k+1)+(t+t))*s;
			// dirty trick, b=0 might go faster
			b = ((k+1)*(b+t))/((k)+(b+b)+(t+t));
			fn = (pn*b+qn)/(rn*b+sn);
			if (Math.abs(fn-f)<=absTol) {
				/* eh */
				break;
			}
		}
//		org.singinst.uf.common.LogUtil.info(String.format("Z = %20.16g, fn = %20.16g", Z, fn));
		return fn;
	}

	public static double inverseCumulativeProbability(double mean,
			double stdDev, double probability) {
		try {
			return new NormalDistributionImpl(mean, stdDev).inverseCumulativeProbability(probability);
		} catch (MathException e) {
			throw new RuntimeException(e);
		}
	}

	       static final double EXPM1MX_IS_1_OVER_64_NEG = -0.18214213554545234604d;
	       static final double EXPM1MX_IS_1_OVER_64_POS =  0.17171823868001516350d;
	public static double expm1mx(double x) {
		if (x>EXPM1MX_IS_1_OVER_64_POS || x<EXPM1MX_IS_1_OVER_64_NEG) {
			return Math.expm1(x)-x;
		} else {
			return ((((((((((1/39916800d)*x+1/3628800d)*x+1/362880d)*x+1/40320d)*x+1/5040d)*x+1/720d)*x+1/120d)*x+1/24d)*x+1/6d)*x+1/2d)*x*x;
		}
	}
	public static double expm1mxox(double x) {
		if (x>EXPM1MX_IS_1_OVER_64_POS || x<EXPM1MX_IS_1_OVER_64_NEG) {
			return (Math.expm1(x)-x)/x;
		} else {
			return ((((((((((1/39916800d)*x+1/3628800d)*x+1/362880d)*x+1/40320d)*x+1/5040d)*x+1/720d)*x+1/120d)*x+1/24d)*x+1/6d)*x+1/2d)*x;
		}
	}
	public static double exprel_2(double x) {
		if (x>EXPM1MX_IS_1_OVER_64_POS || x<EXPM1MX_IS_1_OVER_64_NEG) {
			return (Math.expm1(x)-x)/x/x;
		} else {
			return ((((((((((1/39916800d)*x+1/3628800d)*x+1/362880d)*x+1/40320d)*x+1/5040d)*x+1/720d)*x+1/120d)*x+1/24d)*x+1/6d)*x+1/2d);
		}
	}
	public static double exprel(double x) {
		if (x!=0) {
			return Math.expm1(x)/x;
		}
		else {
			return 1;
		}
	}
	
	
	static final double INTEGRAL_EXP_INTEGRAL_EXP_2O_LIM = 2.7878174747222281e-11d/24; 
	public static double rpn_b_expm1_a_t_a_expm1_b_t_m_a_b_p_3_d_exp_d_a_d_b_d_a_b_m_d(double a, double b) {
		//return (b*Math.expm1(a)-a*Math.expm1(b))/Math.exp((a+b)/3)/a/b/(a-b);
		double s = (a*a + b*b - a*b)*(1/3d);

		if (s<INTEGRAL_EXP_INTEGRAL_EXP_2O_LIM) {
			return 0.5+s*(1/24d);
		}
		
		if (s>24) {
			double amb = a-b;
		    if (Math.abs(amb)>Math.abs(a) && Math.abs(amb)>Math.abs(a)) {
		    	return Math.exp((-1/3d)*(a+b))*(expm1mxox(a)-expm1mxox(b))/(amb);
		    } else if (Math.abs(a)>Math.abs(b)) {
		    	return Math.exp((1/3d)*(-amb+b))*(expm1mxox(amb)-expm1mxox(-b))/a;
		    } else {
		    	return Math.exp((1/3d)*(amb+a))*(expm1mxox(-amb)-expm1mxox(-a))/b;
		    }
		}
		
		double t = ((a-b+a)*(b-a+b)*(a+b))*(-1/27d);
		
		double r2 = s; double r1 = t; double r0 = s*s;
		double d = 720;
		double c = r0/d;
		boolean r0sm = false, rnsm = false;
		for (int i=7; i<60; ++i) {
			double rn = t*r2 + s*r1;
		    r2 = r1; r1 = r0; r0 = rn; d = d*i;
		    rn = rn/d;
		    c = c + rn;
		    r0sm = rnsm; rnsm = Math.abs(rn)<DOUBLE_EPS;
		    if (r0sm && rnsm) { break; }
		}
		return ((c + t/120) + s/24) + 1/2d;
	
	}
	
	public static final double log_plus(double a, double b) {
		if (a<b) return b+Math.log1p(Math.exp(a-b));
		else return a+Math.log1p(Math.exp(b-a));
	}
	
	public static final double round(double value, int decimalPlaces) {
		if (!Double.isInfinite(value) & !Double.isNaN(value)) {
			return new BigDecimal(value).setScale(decimalPlaces, RoundingMode.HALF_EVEN).doubleValue();
		} else {
			return value;
		}
	}

	public static double otherExtreme(double extreme, double average) {
		return average * 2 - extreme;
	}

//	public static double[][] clogCompetingEvents(double[] t, double[][] clog_p ) {
//		
//	}
	
	public static double interpolate(SimplePoint p1,
			SimplePoint p2, double x) {
		double slope = (p2.y - p1.y) / (p2.x - p1.x);
		return p1.y + slope * (x - p1.x);
	}

	public static double bound(double value, double lowest, double highest) {
		if (value < lowest || Double.isNaN(value)) {
			return lowest;
		} else if (value > highest) {
			return highest;
		} else {
			return value;
		}
	}

	public static double average(double... doubleArray) {
		double total = 0;
		for (double d : doubleArray) {
			total += d;
		}
		return total / doubleArray.length;
	}
	
	       static double DOUBLE_SINH_T_EQUALS_T_THRESHOLD = Math.sqrt(3*DOUBLE_EPS);
	static double DOUBLE_NEG_HALF_LOG_EPSILON = -Math.log(DOUBLE_EPS)/2;
	public static double exponentialIntervalAverage (double k0, double k1) {
		double t = Math.abs((k0-k1)/2);
		if (t<DOUBLE_SINH_T_EQUALS_T_THRESHOLD) {
			return Math.exp((k0+k1)/2);
		} else if (t>=DOUBLE_NEG_HALF_LOG_EPSILON) {
			return Math.exp(Math.max(k0, k1))/(t*2);
		} else {
			return Math.exp((k0+k1)/2)*(Math.sinh(t)/t);
		}		
	}
	public static double exponentialIntervalIntegral (double k0, double k1, double width) {
		return width*exponentialIntervalAverage(k0,k1);
	}
	
	static double LOG_DOUBLE_MAX = Math.log(Double.MAX_VALUE);
	static double DOUBLE_MIN_NORMAL = 2.2250738585072014E-308; // = MathUtil.DOUBLE_MIN_NORMAL;
	static double LOG_DOUBLE_MIN_NORMAL = Math.log(DOUBLE_MIN_NORMAL);
	static double LOG_TWO = Math.log(2.0d);
	public static double weightedClogMix(double w0, double w1, double clog_p0, double clog_p1) {
		// disaster zone
		double clog_p;
		if(false) {
		if (clog_p0 < clog_p1) {
			clog_p = -Math.log((w0+w1*Math.exp(clog_p0-clog_p1))/(w0+w1))+clog_p0;
			if ((clog_p-clog_p0)<LOG_TWO) {
				clog_p = -Math.log1p((w0+w1*Math.expm1(clog_p0-clog_p1))/(w0+w1))+clog_p0;
				clog_p -= 100;
			}
		} else {
			clog_p = -Math.log((w0*Math.exp(clog_p1-clog_p0)+w1)/(w0+w1))+clog_p1;
			if ((clog_p-clog_p1)<LOG_TWO) {
				clog_p = -Math.log1p((w0*Math.expm1(clog_p1-clog_p0)+w1)/(w0+w1))+clog_p1;
				clog_p -= 100;
			}
			clog_p -= 200;
		}
		
		clog_p = clog((w0*expc(clog_p0)+w1*expc(clog_p1))/(w0+w1));
		clog_p = -Math.log1p(-(w0*-Math.expm1(-clog_p0)+w1*-Math.expm1(-clog_p1))/(w0+w1));
	    }
	
			clog_p = -Math.log1p((w0*Math.expm1(-clog_p0)+w1*Math.expm1(-clog_p1))/(w0+w1));
		//double w0_times_expm1_neg_clog_p0 = w0*Math.expm1(-clog_p0);
		//double w1_times_expm1_neg_clog_p1 = w1*Math.expm1(-clog_p1);
		//	clog_p = -Math.log1p((w0_times_expm1_neg_clog_p0+w1_times_expm1_neg_clog_p1)/(w0+w1));
		if (clog_p < LOG_TWO) {
			return clog_p;
		} else if (w0 == 0 && w1 != 0) {
			return clog_p1;
		} else if (w0 != 0 && w1 == 0) {
			return clog_p0;
		}
		double w0_times_exp_neg_clog_p0 = w0*Math.exp(-clog_p0);
		double w1_times_exp_neg_clog_p1 = w1*Math.exp(-clog_p1);
		double clog_p_by_log_exp = -Math.log((w0*Math.exp(-clog_p0)+w1*Math.exp(-clog_p1))/(w0+w1));		
		if (w0_times_exp_neg_clog_p0 >= MathUtil.DOUBLE_MIN_NORMAL && w1_times_exp_neg_clog_p1 >= MathUtil.DOUBLE_MIN_NORMAL && clog_p_by_log_exp > Double.NEGATIVE_INFINITY && clog_p_by_log_exp < Double.POSITIVE_INFINITY) {
			return clog_p_by_log_exp;
		} else if /*(w1/w0 == 0 || w0/w1 == 0)*/ (true) {
			double l_w0 = Math.log(w0);
			double l_w1 = Math.log(w1);
			
			double m0 = -clog_p0 + l_w0;
			double m1 = -clog_p1 + l_w1;

			return log_plus(l_w0,l_w1)-log_plus(m0,m1);
			
		} else
			
			/* if (
				  (clog_p0-clog_p1) < LOG_DOUBLE_MAX
				&&(clog_p0-clog_p1) > LOG_DOUBLE_MINNORMAL
				&&(clog_p0-clog_p1-Math.log(w0/w1)) < LOG_DOUBLE_MAX
				&&(clog_p0-clog_p1-Math.log(w0/w1)) > 0
				) {
		} else */ {
			
			if (true) {
			//double discrim = clog_p0 + Math.log(Math.abs(w0)) - clog_p1 - Math.log(Math.abs(w1));
			//double discrim = -clog_p0 + Math.log(Math.abs(w0)) + clog_p1 - Math.log(Math.abs(w1));
			double discrim = Math.abs(clog_p0) + Math.abs(clog_p0-clog_p1-Math.log1p(Math.abs(w0/w1)))
			               - Math.abs(clog_p1) - Math.abs(clog_p1-clog_p0-Math.log1p(Math.abs(w0/w1)));
			double center0_err =
							     Math.abs(clog_p0)
							   + Math.abs(1/(1+(Math.expm1(clog_p0-clog_p1))/(w0/w1+1)))
							   * ( Math.abs(1/(w0/w1+1)) *
								   ( Math.abs(clog_p0-clog_p1)
								   + Math.abs(clog_p0)
								   + Math.abs(clog_p1) )
								 + Math.abs(Math.expm1(clog_p0-clog_p1)/(w0/w1+1)/(w0/w1+1) ) *
								   ( Math.abs(1) ) );
			if (discrim < 0) {
				clog_p = clog_p0-Math.log1p((Math.expm1(clog_p0-clog_p1))/(w0/w1+1));
				//clog_p = clog_p0-Math.log1p((w1*Math.expm1(clog_p0-clog_p1))/(w0+w1));
			} else {
				clog_p = clog_p1-Math.log1p((Math.expm1(clog_p1-clog_p0))/(1+w1/w0));
				//clog_p = clog_p1-Math.log1p((w0*Math.expm1(clog_p1-clog_p0))/(w0+w1));
			}
			}
		}
		
		return clog_p;
	}
	public static double[] weightedClogMix(double w0, double w1, double[] clog_p0, double[] clog_p1) {
		double[] clog_p = new double[clog_p1.length];
		for (int i=0; i<clog_p.length; ++i)
			clog_p[i] = weightedClogMix(w0, w1, clog_p0[i], clog_p1[i]);
		return clog_p;
	}

	static double LOG_THREE =  Math.log(3d);
	public static double weightedLogitMix(double w0, double w1, double logit_p0, double logit_p1) {
		double w = w0+w1;
		if ( !(w<0) && !(w>0) ) { return 0; }
		if (logit_p0 <= -LOG_THREE && logit_p1 <= -LOG_THREE) {
			return weightedLogitMixII(w0, w1, logit_p0, logit_p1);
		} else if (logit_p0 >= LOG_THREE && logit_p1 >= LOG_THREE) {
			return -weightedLogitMixII(w0, w1, -logit_p0, -logit_p1);
		}
		double p0z, p1z;
		if (logit_p0 >= 0) {
			p0z = -Math.expm1(-logit_p0)/(1+Math.exp(-logit_p0));
		} else {
			p0z = Math.expm1(logit_p0)/(1+Math.exp(logit_p0));
		}
		if (logit_p1 >= 0) {
			p1z = -Math.expm1(-logit_p1)/(1+Math.exp(-logit_p1));
		} else {
			p1z = Math.expm1(logit_p1)/(1+Math.exp(logit_p1));
		}
		double pz = (w0*p0z+w1*p1z)/w;
		if (Math.abs(pz)<=0.5) {
			return Math.log1p(pz)-Math.log1p(-pz);
		}
		if (pz<0) {
			return weightedLogitMixII(w0, w1, logit_p0, logit_p1);
		} else {
			return -weightedLogitMixII(w0, w1, -logit_p0, -logit_p1);
		}		
	}
	       static double weightedLogitMixII(double w0, double w1, double logit_p0, double logit_p1) {
		double lp;
		double lp0 = logit_p0-Math.log1p(Math.exp(logit_p0));
		double lp1 = logit_p1-Math.log1p(Math.exp(logit_p1));
		if (Math.abs(w0)<=Math.abs(w1)) {
			if (lp0 <= lp1) {
				lp = lp1 + Math.log1p((Math.expm1(lp0-lp1)*w0/w1)/(1+w0/w1));
			} else {
				lp = lp0 + Math.log1p(Math.expm1(lp1-lp0)/(1+w0/w1));
			}
		} else {
			if (lp0 <= lp1) {
				lp = lp1 + Math.log1p(Math.expm1(lp0-lp1)/(1+w1/w0));
			} else {
				lp = lp0 + Math.log1p((Math.expm1(lp1-lp0)*w1/w0)/(1+w1/w0));
			}
		}
		return lp-Math.log(-Math.expm1(lp));
		/*
		if (logit_p0 >= 0 && logit_p1 >= 0) {
			double t0 = (Math.exp(logit_p1)+1)*w0;
			double t1 = (Math.exp(logit_p0)+1)*w1;
			o = Math.log1p((t0*Math.expm1(logit_p0)+t1*Math.expm1(logit_p1))/(t0+t1));
			if (o > LOG_THREE) {
				o = -Math.log((t0*Math.exp(-logit_p0)+t1*Math.exp(-logit_p1))/(t0+t1));
			}
			return o;
		} else if (logit_p0 < 0 && logit_p1 < 0) {
			double t0 = (Math.exp(-logit_p1)+1)*w0;
			double t1 = (Math.exp(-logit_p0)+1)*w1;
			o = -Math.log1p((t0*Math.expm1(-logit_p0)+t1*Math.expm1(-logit_p1))/(t0+t1)); 
			if (o < -LOG_THREE) {
				o = Math.log((t0*Math.exp(logit_p0)+t1*Math.exp(logit_p1))/(t0+t1));
			}
		}
		double t0 = (Math.exp(logit_p1)+1)*w0;
		double t1 = (Math.exp(logit_p0)+1)*w1;
		return Math.log((t0*Math.exp(logit_p0)+t1*Math.exp(logit_p1))/(t0+t1));
		*/ 			
	}

	
	public static double clogIntervalAverage(double clog_p0, double clog_p1) {
		double d = Math.abs(clog_p0-clog_p1);
		double b = Math.min(clog_p0,clog_p1);
		if (d > 0.25) {
			return b-Math.log(-Math.expm1(-d)/d);			
		} else {
			double dd = d*d;
			return (clog_p0+clog_p1)/2+(-1/24d)*dd*(1+(-1/120d)*dd*(1+(-1/63d)*dd*(1+(-3/160d)*dd*(1+(-2/99d)*dd))));
		}
	}
	
	/**
	 * 
	 * @param t0 (assume cumulative probability of event is 0 at t0)
	 * @param t1
	 * @param mlr0 mean log hazard rate at t0
	 * @param mlr1 mean log hazard rate at t1 
	 * @param sdlr0 standard deviation log hazard rate at t0
	 * @param sdlr1 standard deviation log hazard rate at t1 (can have opposite sign to sdlr0)
	 * @param t
	 * @return array of complementary log probabilities of event by times t
	 */
	public static double[] clogMarginalOfExponentiallyVaryingHazardModel (
			double t0, double t1, 
			double mlr0, double mlr1, 
			double sdlr0, double sdlr1,
			double[] t) {
		double[] clog_p = new double[t.length]; 
		for (int i=0; i<t.length; ++i) {
			double mlr_i = linterp(t0, t1, mlr0, mlr1, t[i]);
			double sdlr_i = linterp(t0, t1, sdlr0, sdlr1, t[i]);
			double clog_p_i = 0;
			double wt = 0;
			int j = -100;
			double z_l;
			double z_r = j/10d;
			double clog_d_l;
			double clog_d_r = exponentialIntervalIntegral(mlr0+z_r*sdlr0, mlr_i+z_r*sdlr_i, t[i]-t0);
			for (++j; j<100; ++j) {
				z_l = z_r;
				z_r = j/10d;
				clog_d_l = clog_d_r;
				clog_d_r = exponentialIntervalIntegral(mlr0+z_r*sdlr0, mlr_i+z_r*sdlr_i, t[i]-t0);
				double wt_z = exponentialIntervalIntegral(-z_l*z_l/2, -z_r*z_r/2, 1/10d);
				clog_p_i = weightedClogMix(wt, wt_z, clog_p_i, clogIntervalAverage(clog_d_l,clog_d_r));
				wt += wt_z;
			}
			clog_p[i] = Math.max(clog_p_i, 0);
		}
		return clog_p;
	}
	
	/**
	 * a one-dimensional family of 5-dimensional weighted integrals of a 5-dimensional step function...
	 * @param m
	 * @param scienceEvents
	 * @param rescheduledYears
	 * @param iters
	 * @return rescheduled_science_events
	 */
	public static EventDiscreteDistributionSchedule clogMarginalScienceSpeedEventRescheduling(
			final ScienceSpeedModelParameters m,
			final EventDiscreteDistributionSchedule scienceEvents,
			final double[] rescheduledYears,
			int iters
	) {
		int n = rescheduledYears.length;
		int e = scienceEvents.logitSubevents.length;
		EventDiscreteDistributionSchedule retVal = new EventDiscreteDistributionSchedule();
		retVal.time = rescheduledYears;
		retVal.clogProbEvent = new double[n];
		retVal.logitSubevents = new double[e][];
		
		// Monte Carlo
		double sequencedata_year_Z=0, sequencedata_factor_Z=0, population_year_Z=0, population_rate_Z=0, institutional_factor_Z=0;
		double dt0_seq, m_seq, b_seq;
		double dt0_pop, m_pop, b_pop;
		double m_inst;
		double dt, science_years=0;
		EventDiscreteDistribution ej;
		int i, j;//, k;
//		double tn;
		for (i=0; i<iters; ++i) {
			if (i%2==0) {
			sequencedata_year_Z = RANDOM.nextGaussian();
			sequencedata_factor_Z = RANDOM.nextGaussian();
			population_year_Z = RANDOM.nextGaussian();
			population_rate_Z = RANDOM.nextGaussian();
			institutional_factor_Z = RANDOM.nextGaussian();
			} else {
				sequencedata_year_Z = -sequencedata_year_Z;
				sequencedata_factor_Z = -sequencedata_factor_Z;
				population_year_Z = -population_year_Z;
				population_rate_Z = -population_rate_Z;
				institutional_factor_Z = -institutional_factor_Z;				
			}
			//sequencedata_year_Z = MathUtil.inverseCumulativeProbability(0, 1, (i+0.5d)/iters);
			dt0_seq = m.sequencedata_base_year + Math.exp(m.sequencedata_mean_log_year + sequencedata_year_Z * m.sequencedata_stddev_log_year) - m.institutional_base_year;
			m_seq = m.sequencedata_mean_slope_log_factor + sequencedata_factor_Z * m.sequencedata_stddev_slope_log_factor;
			b_seq = m.sequencedata_mean_init_log_factor + sequencedata_factor_Z * m.sequencedata_stddev_init_log_factor;
			dt0_pop = m.population_base_year + Math.exp(m.population_mean_log_year + population_year_Z * m.population_stddev_log_year) - m.institutional_base_year;
			m_pop = m.population_mean_slope_log_rate + population_rate_Z * m.population_stddev_slope_log_rate;
			b_pop = m.population_mean_init_log_rate + population_rate_Z * m.population_stddev_init_log_rate;
			m_inst = m.institutional_mean_slope_log_factor + institutional_factor_Z * m.institutional_stddev_slope_log_factor;
			for (j=0; j<n; ++j) {
				dt = rescheduledYears[j] - m.institutional_base_year;
				science_years = exprel( m_inst * dt ) * dt;
				if (dt>dt0_pop)
					science_years += (dt-dt0_pop)*(dt-dt0_pop) * Math.exp( m_inst*((1*dt0_pop+2*dt)/3/* - 0 */) + m_seq*((2*dt0_pop+1*dt)/3-dt0_seq)+b_seq + m_pop*((2*dt0_pop+1*dt)/3-dt0_pop)+b_pop ) * rpn_b_expm1_a_t_a_expm1_b_t_m_a_b_p_3_d_exp_d_a_d_b_d_a_b_m_d( -m_inst*(dt-dt0_pop), (m_pop+m_seq)*(dt-dt0_pop) ); 
					//science_years += (dt-dt0_pop)*(dt-dt0_pop) * Math.exp( m_inst*((1*dt0_pop+2*dt)/3/* - 0 */) + m_seq*((2*dt0_pop+1*dt)/3-dt0_seq)+b_seq + m_pop*((2*dt0_pop+1*dt)/3-dt0_pop)+b_pop ) * -threecancel( -(m_inst)*(t-t0_pop), (m_pop+m_seq)*(t-t0_pop) );
					//science_years += (dt-dt0_pop)*(dt-dt0_pop) * Math.exp( m_inst*((1*dt0_pop+2*dt)/3/* - 0 */) + (m_seq+m_pop)*((1*dt0_pop+2*dt)/3-dt0_seq)+b_seq+b_pop  ) * rpn_b_expm1_a_t_a_expm1_b_t_m_a_b_p_3_d_exp_d_a_d_b_d_a_b_m_d( -(m_seq+m_inst)*(dt-dt0_pop), m_pop*(dt-dt0_pop) ) ;
				//science_years = org.singinst.uf.math.MathUtilTest.projectScienceYearsAnalytic(org.singinst.uf.math.MathUtilTest.applyScienceSpeedZValues(m, new ScienceSpeedZValues()), new double[] {dt})[0];
				ej = scienceEvents.logitnerp(science_years + m.institutional_base_year);
				ej = EventDiscreteDistribution.weightedMix(i/(i+1d), 1/(i+1d), retVal.get(j), ej);
				retVal.set(j, ej);
			}
			//org.singinst.uf.common.LogUtil.info(String.format("random %g leads to progress %g with dt0_seq=%+8.5f, m_seq=%+8.5f, b_seq=%+8.5f, dt0_pop=%+8.5f, m_pop=%+8.5f, b_pop=%+8.5f, m_inst=%+8.5f", dt0_pop, science_years, dt0_seq, m_seq, b_seq, dt0_pop, m_pop, b_pop, m_inst));
		}
		
		return retVal;
	}
	
	public static final Random RANDOM = new Random(1); /* not thread-safe? */
	public static final double NINETY_FIVE_PERCENTILE = 1.64485362695147;

	static public class ScienceSpeedModelParameters extends Object implements Cloneable {
		public double
			sequencedata_base_year,
			sequencedata_mean_log_year,
			sequencedata_stddev_log_year,
			sequencedata_mean_init_log_factor,
			sequencedata_stddev_init_log_factor,
			sequencedata_mean_slope_log_factor,
			sequencedata_stddev_slope_log_factor,
			population_base_year,
			population_mean_log_year,
			population_stddev_log_year,
			population_mean_init_log_rate,
			population_stddev_init_log_rate,
			population_mean_slope_log_rate,
			population_stddev_slope_log_rate,
			institutional_base_year,
			institutional_mean_slope_log_factor,
			institutional_stddev_slope_log_factor;
		public ScienceSpeedModelParameters clone() {
			try { return (ScienceSpeedModelParameters)(super.clone()); }
			catch (CloneNotSupportedException e) { throw new InternalError(e.getMessage()); }
		}
	}
		
	static public class EventDiscreteDistributionSchedule {
		public double[] time;
		public double[] clogProbEvent;
		public double[][] logitSubevents;
		public EventDiscreteDistribution get(int j) {
			EventDiscreteDistribution o = new EventDiscreteDistribution();
			o.clogProbEvent = clogProbEvent[j];
			o.logitSubevents = new double[logitSubevents.length];
			for (int i=0; i<logitSubevents.length; ++i) {
				o.logitSubevents[j] = logitSubevents[i][j];
			}
			return o;
		}
		public void set(int j, EventDiscreteDistribution e) {
			clogProbEvent[j] = e.clogProbEvent;
			for (int i=0; i<logitSubevents.length; ++i) {
				logitSubevents[i][j] = e.logitSubevents[j];
			}
		}
		public EventDiscreteDistribution logitnerp(double t) {
			EventDiscreteDistribution o = new EventDiscreteDistribution();
			o.clogProbEvent = MathUtil.linterp(time, clogProbEvent, t);
			o.logitSubevents = new double[logitSubevents.length];
			for (int i=0; i<logitSubevents.length; ++i) {
				o.logitSubevents[i] = MathUtil.linterp(time, logitSubevents[i], t);
			}
			return o;
		}
		public EventDiscreteDistributionSchedule logitnerp(double[] t) {
			EventDiscreteDistributionSchedule o = new EventDiscreteDistributionSchedule();
			o.clogProbEvent = MathUtil.linterp(time, clogProbEvent, t);
			o.logitSubevents = new double[logitSubevents.length][];
			for (int i=0; i<logitSubevents.length; ++i) {
				o.logitSubevents[i] = MathUtil.linterp(time, logitSubevents[i], t);
			}
			return o;
		}
		static public EventDiscreteDistributionSchedule weightedMix(double w0, double w1, EventDiscreteDistributionSchedule s0, EventDiscreteDistributionSchedule s1) {
			EventDiscreteDistributionSchedule o = new EventDiscreteDistributionSchedule();
			int j,i,m,n;
			n = s0.clogProbEvent.length;
			m = s0.logitSubevents.length;
			o.clogProbEvent = new double[n];
			o.logitSubevents = new double[m][];
			for (j=0; j<m; ++j)
				o.logitSubevents[j] = new double[n];
			for (i=0; i<n; ++i)
				o.set(i, EventDiscreteDistribution.weightedMix(w0,w1,s0.get(i),s1.get(i)) );
			
			return o;
		}
	}

	static public class ScienceSpeedScenario {
		public double
			sequencedata_year,
			sequencedata_init_log_factor,
			sequencedata_slope_log_factor,
			population_year,
			population_init_log_rate,
			population_slope_log_rate,
			institutional_base_year,
			institutional_slope_log_factor;
	}

	static public class ScienceSpeedScenarioReduced {
		public double
			institutional_slope_log_factor,
			sciencetalent_rel_year,
			sciencetalent_init_log_rate,
			sciencetalent_slope_log_rate;
	}

	static public class ScienceSpeedZValues {
		public double
			sequencedata_year_Z,
			sequencedata_factor_Z,
			population_year_Z,
			population_rate_Z,
			institutional_rate_Z;
	}

	static public class EventDiscreteDistribution {
		public double clogProbEvent;
		public double[] logitSubevents;
		public static EventDiscreteDistribution weightedMix(double w0, double w1, EventDiscreteDistribution e0, EventDiscreteDistribution e1) {
			EventDiscreteDistribution o = new EventDiscreteDistribution();
			o.clogProbEvent = MathUtil.weightedClogMix(w0, w1, e0.clogProbEvent, e1.clogProbEvent);
			o.logitSubevents = new double[e0.logitSubevents.length];
			double lw0, lw1;
			lw0 = Math.log(w0) + Math.log1p(-Math.exp(-e0.clogProbEvent));
			lw1 = Math.log(w1) + Math.log1p(-Math.exp(-e1.clogProbEvent));
			for (int i=0; i<e0.logitSubevents.length; ++i) {
				if (lw0>lw1) {
					lw1 = lw1 - lw0; lw0 = 0;
				} else {
					lw0 = lw0 - lw1; lw1 = 0;
				}
				o.logitSubevents[i] = MathUtil.weightedLogitMix(Math.exp(lw0), Math.exp(lw1), e0.logitSubevents[i], e1.logitSubevents[i]);
				lw0 = lw0 + MathUtil.log_logistic(-e0.logitSubevents[i]);
				lw1 = lw1 + MathUtil.log_logistic(-e1.logitSubevents[i]);
			}
			return o;
		}
	}


}

