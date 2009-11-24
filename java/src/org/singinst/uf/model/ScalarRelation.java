package org.singinst.uf.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.ScalarValuePointList;


public abstract class ScalarRelation implements SummarySource {
	
	public ScalarRelation(Axis domain, Axis range) {
		this(domain, range, EMPTY_POINTS);
	}
	
	public ScalarRelation(Axis domain, Axis range, Collection<SimplePoint> decorationPoints) {
		this.domain = domain;
		this.range = range;
		this.decorationPoints = decorationPoints;
	}
	
	public abstract List<? extends ScalarValuePointList> getPointLists();

	public Axis getDomain() {
		return domain;
	}

	public Axis getRange() {
		return range;
	}
	
	private final Axis domain;
	private final Axis range;
	public final Collection<SimplePoint> decorationPoints;
	
	private static final Collection<SimplePoint> EMPTY_POINTS = Collections.emptyList();

}
