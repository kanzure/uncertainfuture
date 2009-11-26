package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.presenter.HtmlUtil;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.LineBounds;

class MooresLawNodeMetadataContentsFactory {
	private final List<MooreAsymptote> asymptotes = new ArrayList<MooreAsymptote>();
	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final List<ScalarValueHolder> scalarValueHolders = new ArrayList<ScalarValueHolder>();
	private final ScalarRelation relation;
	
	public MooresLawNodeMetadataContentsFactory(Node node) {
		LineBounds yearDomain = new LineBounds(1950, 2070);
		Axis year = new Axis(yearDomain);
		Axis flopsPerDollar = new Axis(new LineBounds(-4, 18, true));

		int asymptoteLowerBound = 8;
		for (NotablePercentile percentile : NotablePercentile.values()) {
			LineBounds asymptoteRange = new LineBounds(asymptoteLowerBound, 18);
			MooreAsymptote asymptote = new MooreAsymptote(node, yearDomain, asymptoteRange, percentile);
			asymptotes.add(asymptote);
			scalarSchemata.add(asymptote.getScalar());
			scalarValueHolders.add(asymptote.getConjecture());
			asymptoteLowerBound += 2;
		}
		new MooreConstraint(getScalars()).constrain();
		
		relation = new ScalarRelation(year, flopsPerDollar, MooresLawData.getInstance().points) {

			public List<? extends ConclusionReportGenerator> getConclusionGenerators() {
				return Collections.singletonList(new ConclusionReportGenerator() {

					public String getText(ScalarValueHolder scalarValueHolder, double value) {
						return "The limiting value of log(FLOPS/dollar) has a 90% chance of being between " +
						HtmlUtil.green(getScalars().get(0).getScalarValueHolder().getValue()) + 
						" and " + HtmlUtil.red(getScalars().get(2).getScalarValueHolder().getValue());
					}

				});
			}

			public List<ScalarValueHolder> getScalarValues() {
				return scalarValueHolders;
			}

			@Override
			public List<ScalarValuePointList> getPointLists() {
				List<ScalarValuePointList> retVal = new ArrayList<ScalarValuePointList>();
				for (MooreAsymptote asymptote : asymptotes) {
					retVal.addAll(asymptote.getPointLists());
				}
				return retVal;
			}			
		};

	}
	public ScalarRelation getRelation() {
		return relation;
	}
	public List<ScalarSchema> getScalars() {
		return scalarSchemata;
	}
}
