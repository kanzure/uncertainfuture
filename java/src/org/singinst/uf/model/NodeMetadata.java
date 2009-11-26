package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.List;

import org.singinst.uf.math.MathUtil;

public class NodeMetadata {
	private final Node node;
	private final UiText uiText;
	private final List<ScalarSchema> scalarSchemata;
	private final List<ScalarSchema> simpleScalars; // never used
	private final List<ScalarRelation> scalarRelations;
	private final List<ScalarValueHolder> scalarValueHolders;

	public NodeMetadata(Node node, UiText uiText, List<ScalarSchema> scalarSchemata, List<ScalarSchema> simpleScalars, List<ScalarRelation> scalarRelations) {
		this.node = node;
		this.uiText = uiText;
		this.scalarSchemata = scalarSchemata;
		this.simpleScalars = simpleScalars;
		this.scalarRelations = scalarRelations;
		this.scalarValueHolders = new ArrayList<ScalarValueHolder>();
		for (ScalarSchema schema : scalarSchemata) {
			scalarValueHolders.add(schema.getScalarValueHolder());
		}
	}

	public List<ScalarSchema> getScalars() {
		return scalarSchemata;
	}

	public Node getNode() {
		return node;
	}

	public List<ScalarRelation> getScalarRelations() {
		return scalarRelations;
	}

	public List<ScalarSchema> getSimpleScalars() {
		return simpleScalars;
	}

	public UiText getUserText() {
		return uiText;
	}

	public Object monte() {
		if (!simpleScalars.isEmpty()) {
			return simpleScalars.get(0).monte();
		} else if (scalarSchemata.size() == 2) {
			double mean = scalarSchemata.get(0).getScalarValueHolder().getValue();
			double stdDev = scalarSchemata.get(1).getScalarValueHolder().getValue();
			return MathUtil.inverseCumulativeProbability(mean, stdDev, MathUtil.RANDOM.nextFloat());
		} else {
			double k5 = scalarSchemata.get(0).getScalarValueHolder().getValue();
			double k50 = scalarSchemata.get(1).getScalarValueHolder().getValue();
			return MathUtil.inverseCumulativeProbability(
					k50, (k50 - k5) / MathUtil.NINETY_FIVE_PERCENTILE, 
					MathUtil.RANDOM.nextFloat());
		}
	}

	public List<ScalarValueHolder> getScalarValueHolders() {
		return scalarValueHolders;
	}
}
