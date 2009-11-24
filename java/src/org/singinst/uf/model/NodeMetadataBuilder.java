package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.presenter.LineBounds;

class NodeMetadataBuilder {
	private final Node node;
	private UiText uiText;
	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final List<ScalarSchema> simpleScalars = new ArrayList<ScalarSchema>();
	private final List<ScalarRelation> scalarRelations = new ArrayList<ScalarRelation>();
	
	public NodeMetadataBuilder(String name) {
		this(name, Collections.<NodeMetadata>emptyList());
	}

	public NodeMetadataBuilder(String name, List<NodeMetadata> dependencies) {
		this(new Node(name, dependencies));
	}

	public NodeMetadataBuilder(Node node) {
		this.node = node;
	}

	public NodeMetadataBuilder setBuilderUserText(UiText uiText) {
		this.uiText = uiText;
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsProbability(ConclusionReportGenerator... generators) {
		LineBounds bounds = new LineBounds(0, 100);
		ScalarSchema scalarSchema = new ScalarSchema(node, ScalarSubIDString.PROBABILITY, bounds, "%", "Probability: ", "", bounds, true);
		scalarSchema.getConclusionGenerators().addAll(Arrays.asList(generators));
		scalarSchemata.add(scalarSchema);
		simpleScalars.add(scalarSchema);
		return this;
	}

	public NodeMetadata build() {
		return new NodeMetadata(node, uiText, scalarSchemata, simpleScalars, scalarRelations);
	}

	public NodeMetadataBuilder buildNodeTypeAsMooresLaw() {
		MooresLawNodeMetadataContentsFactory mooresLawNodeMetadataContentsFactory = new MooresLawNodeMetadataContentsFactory(node);
		scalarSchemata.addAll(mooresLawNodeMetadataContentsFactory.getScalars());
		scalarRelations.add(mooresLawNodeMetadataContentsFactory.getRelation());
		return this;
	}
	
	public NodeMetadataBuilder buildNodeTypeAsHazardRate() {
		HazardRateNodeMetadataContentsFactory hazardRateNodeMetadataContentsFactory = new HazardRateNodeMetadataContentsFactory(node);
		scalarSchemata.addAll(hazardRateNodeMetadataContentsFactory.getScalars());
		scalarRelations.add(hazardRateNodeMetadataContentsFactory.getRelation());
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsGeneLearning() {
		GeneLearningNodeMetadataContentsFactory geneLearningNodeMetadataContentsFactory = new GeneLearningNodeMetadataContentsFactory(node);
		scalarSchemata.addAll(geneLearningNodeMetadataContentsFactory.getScalarSchemata());
		scalarRelations.add(geneLearningNodeMetadataContentsFactory.getRelation());
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsOtherIa() {
		OtherIaNodeMetadataContentsFactory otherIaNodeMetadataPartialPrototype = new OtherIaNodeMetadataContentsFactory(node);
		scalarSchemata.addAll(otherIaNodeMetadataPartialPrototype.getScalars());
		scalarRelations.add(otherIaNodeMetadataPartialPrototype.getRelation());
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsResearcher() {
		ResearchersNodeMetadataContentsFactory researchersNodeMetadataContentsFactory = new ResearchersNodeMetadataContentsFactory(node);
		scalarSchemata.addAll(researchersNodeMetadataContentsFactory.getScalars());
		scalarRelations.add(researchersNodeMetadataContentsFactory.getRelation());
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsLogNormal(String units, double lowerBound, double upperBound) {
		NormalNodeMetadataContentsFactory logNormal = new NormalNodeMetadataContentsFactory(node, units, new LineBounds(lowerBound, upperBound, true), "");
		scalarSchemata.addAll(logNormal.getScalars());
		scalarRelations.add(logNormal.getRelation());
		return this;
	}

	public NodeMetadataBuilder buildNodeTypeAsYearLogNormal(int firstMajorTick, int boundaryYear, int lowerYear, int upperYear) {
		double lowerBound = Math.log10(lowerYear - boundaryYear);
		double upperBound = Math.log10(upperYear - boundaryYear);
		int majorTickSpace = upperYear == ModelUtil.LATEST_IA_YEAR ? 5 : 50;
		LineBounds bounds = new LogBounds(firstMajorTick, boundaryYear, lowerBound, upperBound, majorTickSpace, 10);
		//NormalNodeMetadataContentsFactory yearNormal = new NormalNodeMetadataContentsFactory(node, "(log) years after " + boundaryYear, bounds) {
		NormalNodeMetadataContentsFactory yearNormal = new NormalNodeMetadataContentsFactory(node, "years after " + boundaryYear, bounds, "10 to the") {

			@Override
			protected String meanUnits() {
				return "year";
			}
			
		};
		scalarSchemata.addAll(yearNormal.getScalars());
		scalarRelations.add(yearNormal.getRelation());
		return this;
	}

	public NodeMetadataBuilder addScalarRelation(ScalarRelation relation) {
		scalarRelations.add(relation);
		return this;
	}

}
