package org.singinst.uf.model;

import java.util.List;

public class NormalConstraint {
	private final double lowerBound;
	private final List<ScalarSchema> scalarSchemata;
	private boolean disable;

	public NormalConstraint(List<ScalarSchema> scalarSchemata, double lowerBound) {
		this.scalarSchemata = scalarSchemata;
		this.lowerBound = lowerBound;
	}
	
	private final ValueListener leftAnchorListener = new MooreUpdateListener() {
		void adjust() {
			getRightConjecture().setValue(extrapolate(getLeftConjecture(), getMiddleConjecture()));
		}
	};
	
	private final ValueListener rightAnchorListener = new MooreUpdateListener() {
		void adjust() {
			getLeftConjecture().setValue(extrapolate(getRightConjecture(), getMiddleConjecture()));
		}
	};

	public void constrain() {
		getLeftConjecture().addUpdateListener(leftAnchorListener);
		getMiddleConjecture().addUpdateListener(leftAnchorListener);
		getRightConjecture().addUpdateListener(rightAnchorListener);
	}

	protected double extrapolate(ScalarValueHolder firstConjecture,
			ScalarValueHolder secondConjecture) {
		return secondConjecture.getValue() * 2 - firstConjecture.getValue();
	}

	private ScalarValueHolder getConjecture(int i) {
		return scalarSchemata.get(i).getScalarValueHolder();
	}

	private ScalarValueHolder getLeftConjecture() {
		return getConjecture(0);
	}

	private ScalarValueHolder getMiddleConjecture() {
		return getConjecture(1);
	}

	private ScalarValueHolder getRightConjecture() {
		return getConjecture(2);
	}
	
	private abstract class MooreUpdateListener implements ValueListener {

		public void fireUpdate(double value) {
			if (!disable) {
				disable = true;
				try {
					adjust();
					fixLeftToRight();
				} finally {
					disable = false;
				}
			}
		}

		abstract void adjust();

		private void fixLeftToRight() {
			double left = getLeftConjecture().getValue();
			double right = getRightConjecture().getValue();
			if (left > right) {
				getLeftConjecture().setValue(right);
				getRightConjecture().setValue(left);
			}
			if (getLeftConjecture().getValue() < lowerBound) {
				if (getMiddleConjecture().getValue() < lowerBound) {
					getMiddleConjecture().setValue(lowerBound);
				}
				getLeftConjecture().setValue(lowerBound);
				getRightConjecture().setValue(extrapolate(getLeftConjecture(), getMiddleConjecture()));
			}
		}
		
	}

}
