package org.singinst.uf.model;

import java.util.Arrays;
import java.util.List;

import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;

public class IncidentExtremeNodeMetadataContentsFactory extends YearExtremeNodeMetadataContentsFactory {

	
	protected IncidentExtremeNodeMetadataContentsFactory(Node node, LineBounds rangeBounds,
			double year) {
		super(node, rangeBounds, year, " chance in " + (int) year, "%", "small", "large");
	}

	public static List<IncidentExtremeNodeMetadataContentsFactory> createList(Node node, LineBounds rangeBounds, LineBounds yearBounds) {
		IncidentExtremeNodeMetadataContentsFactory incidentEarly = new IncidentExtremeNodeMetadataContentsFactory(node, rangeBounds, yearBounds.getFirst());
		IncidentExtremeNodeMetadataContentsFactory incidentLate = new IncidentExtremeNodeMetadataContentsFactory(node, rangeBounds, yearBounds.getSecond());
		return Arrays.asList(incidentEarly, incidentLate);
	}

	@Override
	public ConclusionReportGenerator getConclusionGenerator() {
		return new ConclusionReportGenerator() {
			public String getText(ScalarValueHolder scalarValueHolder, double value) {
				return "The logarithm of the instantaneous rate at year " + getYearString() + " has a 90% chance of being between " +
				HtmlUtil.green(getScalars().get(0).getScalarValueHolder().getValue()) + 
				" and " + HtmlUtil.red(getScalars().get(2).getScalarValueHolder().getValue());
			}
		};
	
	}


}
