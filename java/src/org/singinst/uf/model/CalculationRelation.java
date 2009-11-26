package org.singinst.uf.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.Completion;
import org.singinst.uf.presenter.ScalarValuePointList;
import org.singinst.uf.presenter.LineBounds;

public abstract class CalculationRelation extends ScalarRelation {

	private final Node node;

	private final List<ScalarSchema> scalarSchemata = new ArrayList<ScalarSchema>();
	private final List<ScalarValueHolder> scalarValueHolders = new ArrayList<ScalarValueHolder>();
	private final StringBuilder htmlConsole = new StringBuilder();
	private final ScalarValueDependency dependency = new ScalarValueDependency();

	private final ConclusionReportGenerator conclusion = new ConclusionReportGenerator() {

		public String getText(ScalarValueHolder scalarValueHolder, double value) {
			return htmlConsole.toString();
		}

	};

	private final ScalarValuePointList curveList = new ScalarValuePointList() {

		@Override
		public Runnable updater() {
			return new Runnable() {

				public void run() {
					Completion completion = Completion.getInstance();
					if (completion.init("Calculating " + node.getIdString(), ModelUtil.LATEST_YEAR - ModelUtil.EARLIEST_YEAR)) {
						for (int year = ModelUtil.EARLIEST_YEAR; year <= ModelUtil.LATEST_YEAR; year++) {
							ScalarValueHolder scalarValueHolder = ScalarValueHolder.findById(node.getIdString(), ScalarSubIDString.yearProbabilityID(year));
							scalarValueHolder.getValue();
							completion.tick();
						}
					}
				}

			};
		}

	};

	public CalculationRelation(Node node) {
		super(new Axis(new LineBounds(ModelUtil.EARLIEST_YEAR, ModelUtil.LATEST_YEAR)),
				new Axis(new LineBounds(0, 1)));
		this.node = node;
		
		List<Double> yearList = new ArrayList<Double>();
		for (double year = ModelUtil.EARLIEST_YEAR; year <= ModelUtil.LATEST_UNACCELERATED_YEAR; year += ModelUtil.YEAR_STEPSIZE) {
			yearList.add((double) year);
		}
		List<Calculation> calculations = getCalculations(yearList);
		
		for (double year = ModelUtil.EARLIEST_YEAR; year <= ModelUtil.LATEST_UNACCELERATED_YEAR; year += ModelUtil.YEAR_STEPSIZE) {
			ScalarSchema scalarSchema = new ScalarSchema(
					node, ScalarSubIDString.yearProbabilityID(year), new LineBounds(0, 1), "", "", "", null, true);
			scalarSchemata.add(scalarSchema);
			ScalarValueHolder scalarValueHolder = scalarSchema.getScalarValueHolder();
			if (year <= ModelUtil.LATEST_YEAR && ((year % 1d) == 0d) /* could use elementOf to ModelUtil.BASIC_MODEL_YEARS? */ ) {
				scalarValueHolders.add(scalarValueHolder);
			}
			scalarValueHolder.setCalculation(calculations.remove(0));
		}
	}

	protected abstract List<Calculation> getCalculations(List<Double> year);

	@Override
	public List<ScalarValuePointList> getPointLists() {
		List<SimplePoint> pointList = new ArrayList<SimplePoint>();
		ScalarValueHolder lastScalarValue = null;
		Boolean needsCalc = null;
		for (int year = ModelUtil.EARLIEST_YEAR; year <= ModelUtil.LATEST_YEAR; year++) {
			lastScalarValue = ScalarValueHolder.findById(node.getIdString(), ScalarSubIDString.yearProbabilityID(year));
			if (needsCalc == null) {
				if (lastScalarValue.needsCalc(new NeedsCalcCache())) {
					curveList.setHypothesisPoints(null);
					return Collections.singletonList(curveList);
				} else {
					needsCalc = false;
				}
			}
			double value = lastScalarValue.getValue();

			// TODO4
			//				dependency.validate();

			pointList.add(new SimplePoint(year, value));
		}
		if (lastScalarValue != null) {
			StringBuilder newConsoleText = lastScalarValue.getCalculation().getHtmlConsole();;
			if (this instanceof AiRelation)  {
				newConsoleText = new StringBuilder();
				lastScalarValue.getCalculation().setHtmlConsole( newConsoleText );
			}
			
			if (!htmlConsole.toString().equals(newConsoleText.toString())) {
				htmlConsole.setLength(0);
				htmlConsole.append(newConsoleText);
				lastScalarValue.notifyListeners();
			}
		}
		curveList.setHypothesisPoints(pointList);
		return Collections.singletonList(curveList);
	}	

	public List<? extends ConclusionReportGenerator> getConclusionGenerators() {
		return Collections.singletonList(conclusion);
	}

	public List<ScalarValueHolder> getScalarValues() {
		return scalarValueHolders;
	}

	protected ScalarValueDependency getDependency() {
		return dependency;
	}

	protected Calculation probabilityAiIsPossible() {
		return new Calculation("Probability that AI is possible in principle") {
			@Override
			protected double rawEvaluate(StringBuilder htmlConsole) {
				return getDependency().value(NodeIDString.Q1_1, ScalarSubIDString.PROBABILITY) / 100;
			}
			
			@Override
			public Color getColor(){
				return Color.GREEN;
			}
			
			
		};
	}
}
