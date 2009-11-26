package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.math.MathUtil;
import org.singinst.uf.presenter.LineBounded;
import org.singinst.uf.presenter.LineBounds;

public class ScalarSchema implements SummarySource, LineBounded {
	private final String prefix;
	private final String suffix;

	private final Node node;
	private final String idString;
	private final LineBounds bounds;
	private final String unit;
	private final List<ConclusionReportGenerator> conclusionReportGenerators = new ArrayList<ConclusionReportGenerator>();
	private boolean visibleAxis = true;
	private boolean visibleProperty = true;

	private final LineBounds boundsConstraint;
	
	public ScalarSchema(Node node, String idString, LineBounds bounds, String unit, String prefix, String suffix, LineBounds boundsConstraint, boolean visibleProperty) {
		this.node = node;
		this.idString = idString;
		this.bounds = bounds;
		this.unit = unit;
		this.prefix = prefix;
		this.suffix = suffix;
		this.boundsConstraint = boundsConstraint;
		this.visibleProperty = visibleProperty;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}

	public String getKey() {
		return node.getIdString() + "." + idString;
	}
	
	@Override
	public String toString() {
		return getKey();
	}

	public List<ScalarValueHolder> getScalarValues() {
		return Collections.singletonList(getScalarValueHolder());
	}
	public ScalarValueHolder getScalarValueHolder() {
		return ScalarValueHolder.findById(this);
	}
	public List<ConclusionReportGenerator> getConclusionGenerators() {
		return conclusionReportGenerators;
	}
	public LineBounds getLineBounds() {
		return getBounds();
	}
	public LineBounds getBounds() {
		return bounds;
	}
	public boolean isVisibleAxis() {
		return visibleAxis;
	}
	public boolean displayProperty() {
		return visibleProperty;
	}
	public void setVisibleAxis(boolean visibleAxis) {
		this.visibleAxis = visibleAxis;
	}
	public Collection<NodeMetadata> getDependencies() {
		return node.getDependencies();
	}
	
	public static int getMaxDecimalDigits() {
		return 4;
	}

	public String getUnit() {
		return unit;
	}

	public LineBounds getBoundsConstraint() {
		return boundsConstraint;
	}

	public Object monte() {
		return MathUtil.RANDOM.nextFloat() * 100 < getScalarValueHolder().getValue();
	}
}
