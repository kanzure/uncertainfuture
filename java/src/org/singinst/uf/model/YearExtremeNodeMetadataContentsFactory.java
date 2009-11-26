package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.SimpleStyle;

public abstract class YearExtremeNodeMetadataContentsFactory {

	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final Map<NotablePercentile, ScalarSchema> scalarMap = new HashMap<NotablePercentile, ScalarSchema>();
	private final double year;

	protected YearExtremeNodeMetadataContentsFactory(Node node, LineBounds rangeBounds, double year, 
													String subPrefix, String units, String adjSmall, String adjLarge) {
		this.year = year;
		for (NotablePercentile percentile : NotablePercentile.values()) {
			SimpleStyle style = new SimpleStyle(percentile.getColor());
			
			ScalarSchema scalarSchema = new ScalarSchema(
					node, 
					ScalarSubIDString.yearPercentileID(year, percentile.getValue()),
					//"y" + year + "k" + percentile.getValue(), 
					rangeBounds, 
					units, 
					HtmlUtil.style(style, percentile.getText(adjSmall, adjLarge) + subPrefix + ": "),
					//"y" + year + "k" + percentile.getValue(), 
					"", null, true);
			ScalarValueHolder scalarValueHolder = scalarSchema.getScalarValueHolder();
			scalarValueHolder.setValue(scalarValueHolder.getValue() + percentile.getOffset() / 2);
			getScalars().add(scalarSchema);
			scalarMap.put(percentile, scalarSchema);
		}
		new NormalConstraint(scalarSchemata, -50).constrain();
	}

	public List<ScalarSchema> getScalars() {
		return scalarSchemata;
	}

	public ScalarSchema getScalarSchema(NotablePercentile percentile) {
		return scalarMap.get(percentile);
	}

	public double getYear() {
		return year;
	}

	public final String getYearString() {
		return "" + (int) year;
	}

	public abstract ConclusionReportGenerator getConclusionGenerator();
}
