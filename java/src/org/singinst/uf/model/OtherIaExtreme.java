package org.singinst.uf.model;

import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.NumericEntry;

public class OtherIaExtreme extends YearExtremeNodeMetadataContentsFactory {

	protected OtherIaExtreme(Node node, LineBounds rangeBounds,
			double year) {
		super(node, rangeBounds, year, " log speedup in year " + (int) year , "", "low", "high");
	}

	@Override
	public ConclusionReportGenerator getConclusionGenerator() {
		return new ConclusionReportGenerator() {
			public String getText(ScalarValueHolder scalarValueHolder, double value) {
				return "The research speedup in the year " + getYearString() + " has a 90% chance of being between " +
				HtmlUtil.green(display(getScalars().get(0).getScalarValueHolder().getValue())) + 
				" and " + HtmlUtil.red(display(getScalars().get(2).getScalarValueHolder().getValue()));
			}

			private String display(double value) {
				return NumericEntry.formatAsScalar(Math.pow(10, value)) + "x";
			}
		};
	
	}


}
