package org.singinst.uf.model;

public abstract class CompositeCalculation extends Calculation {

	private final Evaluable[] calculations;

	public CompositeCalculation(String description,
			Evaluable... calculations2) {
		super(description, false);
		this.calculations = calculations2;
	}

	public Evaluable[] getCalculations() {
		return calculations;
	}

}
