package org.singinst.uf.model;

import java.util.Arrays;
import java.util.List;

import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;

public class ResearcherExtreme extends YearExtremeNodeMetadataContentsFactory {

	
	protected ResearcherExtreme(Node node, LineBounds rangeBounds,
			double year) {
		super(node, rangeBounds, year, " log #, " + (int) year + " years later", "", "low", "high");
	}

	public static List<ResearcherExtreme> createList(Node node, LineBounds rangeBounds, LineBounds yearBounds) {
		ResearcherExtreme incidentEarly = new ResearcherExtreme(node, rangeBounds, yearBounds.getFirst());
		ResearcherExtreme incidentLate = new ResearcherExtreme(node, rangeBounds, yearBounds.getSecond());
		return Arrays.asList(incidentEarly, incidentLate);
	}

	@Override
	public ConclusionReportGenerator getConclusionGenerator() {
		return new ConclusionReportGenerator() {
			public String getText(ScalarValueHolder scalarValueHolder, double value) {
				return "The number of IA enhanced research scientists " + getYearString() + " years later " +
				"has a 90% chance of being between " +
				HtmlUtil.green(display(getScalars().get(0).getScalarValueHolder().getValue())) + 
				" and " + HtmlUtil.red(display(getScalars().get(2).getScalarValueHolder().getValue()));
			}
			
			private String display(double value) {
				return "" + (int) (Math.pow(10, value));
			}
		};
		
	}


}
