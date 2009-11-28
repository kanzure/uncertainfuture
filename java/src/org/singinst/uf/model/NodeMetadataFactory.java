package org.singinst.uf.model;

import java.util.Arrays;
import java.util.List;

import org.singinst.uf.presenter.NumericEntry;

public class NodeMetadataFactory {
	public static List<NodeMetadata> createTheNetwork() {
		NodeMetadata q1_1 = constructQ1_1Builder().build();
		NodeMetadata q1_2 = constructQ1_2Builder().build();
		NodeMetadata q1_3 = constructQ1_3Builder().build();
		NodeMetadata q1_4 = constructQ1_4Builder().build();
		NodeMetadata q1_5 = constructQ1_5Builder().build();
		NodeMetadata c1_5 = constructC1_5Builder(Arrays.asList(q1_1, q1_2, q1_3, q1_4, q1_5)).build();
		NodeMetadata q1_6 = constructQ1_6Builder().build();
		NodeMetadata c1_6 = constructC1_6Builder(Arrays.asList(c1_5, q1_6)).build();
		NodeMetadata q1_7 = constructQ1_7Builder().build();
		NodeMetadata q1_8 = constructQ1_8Builder().build();
		NodeMetadata c1_8 = constructC1_8Builder(Arrays.asList(q1_7, q1_8)).build();
		NodeMetadata q1_9_1 = constructQ1_9_1Builder().build();
		NodeMetadata q1_9_2 = constructQ1_9_2Builder().build();
		NodeMetadata q1_9_3 = constructQ1_9_3Builder().build();
		NodeMetadata q1_9_4 = constructQ1_9_4Builder().build();
		NodeMetadata q1_9_5 = constructQ1_9_5Builder().build();
		//NodeMetadata c1_9 = constructQ1_9Builder(Arrays.asList(q1_9_1, q1_9_2, q1_9_3, q1_9_4, q1_9_5, c1_6, c1_5)).build();
		NodeMetadata c1_9 = constructQ1_9Builder(Arrays.asList(q1_1, q1_2, q1_3, q1_4, q1_5, q1_9_1, q1_9_2, q1_9_3, q1_9_4, q1_9_5, c1_5, c1_6)).build();
		NodeMetadata c1_10 = constructC1_10Builder(Arrays.asList(c1_8, c1_9)).build();

		// Set default values for graphs
		setNodeScalars(q1_1, 99);
		setNodeScalars(q1_2, 10, 11, 12);
		setNodeScalars(q1_3, 12.5, 2.25);
		setNodeScalars(q1_4, 5, 1);
//		setNodeScalars(q1_5, 2018, 0.005);
		
		setNodeScalars(q1_5, 2.0976, 0.10);
		setNodeScalars(q1_6, 2.0, 0.10);		
		
		//setNodeScalars(q1_6);
		
		return Arrays.asList(new NodeMetadata[] {
				q1_1,
				q1_2,
				q1_3,
				q1_4,
				q1_5,
				c1_5,
				q1_6,
				c1_6,
				q1_7,
				q1_8,
				c1_8,
				q1_9_1,
				q1_9_2,
				q1_9_3,
				q1_9_4,
				q1_9_5,
				c1_9,
				c1_10
		});
	}

	private static void setNodeScalars(NodeMetadata qNotOnly1_1GoesHere, double... dArray) {
		int i = 0;
		for (double d : dArray) {
			ScalarValueHolder scalarValueHolder = qNotOnly1_1GoesHere.getScalars().get(i).getScalarValueHolder();
			scalarValueHolder.setValue(d);
			i++;
		}
		
	}

	private static NodeMetadataBuilder constructQ1_1Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_1)
		.setBuilderUserText(UiTextConstant.Q1_1_TEXT)
		.buildNodeTypeAsProbability(new ConclusionReportGenerator() {

			public String getText(ScalarValueHolder scalarValueHolder, double value) {
				return "The probability AI is possible in principle is " + NumericEntry.formatAsScalar(value) + "%";
			}

		},
		new ConclusionReportGenerator() {

			public String getText(ScalarValueHolder scalarValueHolder, double value) {
				return "The probability AI is not possible in principle is " + NumericEntry.formatAsScalar(100 - value) + "%";
			}

		});
	}

	private static NodeMetadataBuilder constructQ1_2Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_2)
		.setBuilderUserText(UiTextConstant.Q1_2_TEXT)
		.buildNodeTypeAsMooresLaw();
	}

	private static NodeMetadataBuilder constructQ1_3Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_3)
		.setBuilderUserText(UiTextConstant.Q1_3_TEXT)
		.buildNodeTypeAsLogNormal("flops", 7, 30);
	}

	private static NodeMetadataBuilder constructQ1_4Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_4)
		.setBuilderUserText(UiTextConstant.Q1_4_TEXT)
		.buildNodeTypeAsLogNormal("USD", 2, 11);
	}

	private static NodeMetadataBuilder constructQ1_5Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_5)
		.setBuilderUserText(UiTextConstant.Q1_5_TEXT)
		.buildNodeTypeAsYearLogNormal(2050, ModelUtil.ANCHOR_FAR_YEAR, ModelUtil.EARLIEST_FAR_YEAR, ModelUtil.LATEST_FAR_YEAR);
	}

	private static NodeMetadataBuilder constructC1_5Builder(List<NodeMetadata> dependencies) {
		Node node = new Node(NodeIDString.C1_5, dependencies);
		return new NodeMetadataBuilder(node)
		.setBuilderUserText(UiTextConstant.C1_5_TEXT)
		.addScalarRelation(new NeuromorphicAiRelation(node));
	}

	private static NodeMetadataBuilder constructQ1_6Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_6)
		.setBuilderUserText(UiTextConstant.Q1_6_TEXT)
		.buildNodeTypeAsYearLogNormal(2050, ModelUtil.ANCHOR_FAR_YEAR, ModelUtil.EARLIEST_FAR_YEAR, ModelUtil.LATEST_FAR_YEAR);
	}

	private static NodeMetadataBuilder constructC1_6Builder(List<NodeMetadata> dependencies) {
		Node node = new Node(NodeIDString.C1_6, dependencies);
		return new NodeMetadataBuilder(node)
		.setBuilderUserText(UiTextConstant.C1_6_TEXT)
		.addScalarRelation(new AiRelation(node));
	}

	private static NodeMetadataBuilder constructQ1_7Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_7)
		.setBuilderUserText(UiTextConstant.Q1_7_TEXT)
		.buildNodeTypeAsHazardRate();
	}

	private static NodeMetadataBuilder constructQ1_8Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_8)
		.setBuilderUserText(UiTextConstant.Q1_8_TEXT)
		.buildNodeTypeAsHazardRate();
	}

	private static NodeMetadataBuilder constructC1_8Builder(List<NodeMetadata> dependencies) {
		Node node = new Node(NodeIDString.C1_8, dependencies);
		return new NodeMetadataBuilder(node)
		.setBuilderUserText(UiTextConstant.C1_8_TEXT)
		.addScalarRelation(new CumulativeHazardsRelation(node));
	}
	
	private static NodeMetadataBuilder constructQ1_9_1Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_9_1)
		.setBuilderUserText(UiTextConstant.Q1_9_1_TEXT)
		.buildNodeTypeAsYearLogNormal(2010, ModelUtil.ANCHOR_FAR_YEAR, ModelUtil.EARLIEST_IA_YEAR, ModelUtil.LATEST_IA_YEAR);
	}

	private static NodeMetadataBuilder constructQ1_9_2Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_9_2)
		.setBuilderUserText(UiTextConstant.Q1_9_2_TEXT)
		.buildNodeTypeAsYearLogNormal(2010, ModelUtil.ANCHOR_FAR_YEAR, ModelUtil.EARLIEST_IA_YEAR, ModelUtil.LATEST_IA_YEAR);
	}

	private static NodeMetadataBuilder constructQ1_9_3Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_9_3)
		.setBuilderUserText(UiTextConstant.Q1_9_3_TEXT)
		.buildNodeTypeAsGeneLearning();
	}

	private static NodeMetadataBuilder constructQ1_9_4Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_9_4)
		.setBuilderUserText(UiTextConstant.Q1_9_4_TEXT)
		.buildNodeTypeAsResearcher();
	}

	private static NodeMetadataBuilder constructQ1_9_5Builder() {
		return new NodeMetadataBuilder(NodeIDString.Q1_9_5)
		.setBuilderUserText(UiTextConstant.Q1_9_5_TEXT)
		.buildNodeTypeAsOtherIa();
	}

	private static NodeMetadataBuilder constructQ1_9Builder(List<NodeMetadata> dependencies) {
		Node node = new Node(NodeIDString.C1_9, dependencies);
		return new NodeMetadataBuilder(node)
		.setBuilderUserText(UiTextConstant.C1_9_TEXT)
		.addScalarRelation(new RescheduledEventsRelation(node));
	}

	private static NodeMetadataBuilder constructC1_10Builder(List<NodeMetadata> dependencies) {
		Node node = new Node(NodeIDString.C1_10, dependencies);
		return new NodeMetadataBuilder(node)
		.setBuilderUserText(UiTextConstant.C1_10_TEXT)
		.addScalarRelation(new DisruptionRelation(node));
	}
}
