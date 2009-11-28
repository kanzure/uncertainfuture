package org.singinst.uf.model;

import org.singinst.uf.common.StringUtil;
import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.NumericEntry;

public class GeneLearningExtreme extends YearExtremeNodeMetadataContentsFactory {

	protected GeneLearningExtreme(Node node, LineBounds rangeBounds,
			double year) {
		//super(node, rangeBounds, year, " log speedup after " + (int) year + " yrs", "", "early", "late");
		super(node, rangeBounds, year, " log research speedup after " + (int) year + " yrs", "", "small", "large");
	}

	@Override
	public ConclusionReportGenerator getConclusionGenerator() {
		return new ConclusionReportGenerator() {
			public String getText(ScalarValueHolder scalarValueHolder, double value) {
//				return "The research speedup " + getYearString() + " years later has a 90% chance of being between " +
//				HtmlUtil.green(display(getScalars().get(0).getScalarValueHolder().getValue())) + 
//				" and " + HtmlUtil.red(display(getScalars().get(2).getScalarValueHolder().getValue()));
				return "The research speedup " + getYearString() + " years later has a 90% chance of being between " +
				HtmlUtil.green(StringUtil.formatMultiplier(getScalars().get(0).getScalarValueHolder().getValue())) + 
				" and " + HtmlUtil.red(StringUtil.formatMultiplier(getScalars().get(2).getScalarValueHolder().getValue()));
			}

//			private String display(double value) {
//				return NumericEntry.formatAsScalar(Math.pow(10, value)) + "x";
//			}
		};
	
	}


}
