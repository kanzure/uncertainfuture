package org.singinst.uf.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.singinst.uf.common.StringUtil;
import org.singinst.uf.math.MathUtil;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.Store;

public class ScalarValueHolder implements Evaluable {
	@Override
	public String toString() {
		return getScalar().getKey();
	}

	private static final Map<String, ScalarValueHolder> store = new HashMap<String, ScalarValueHolder>();
	private final List<ValueListener> valueListeners = new ArrayList<ValueListener>();
	private double value;
	private final ScalarSchema scalarSchema;
	private double timestamp;
	private Calculation calculation;

	public ScalarValueHolder(ScalarSchema scalarSchema, double value) {
		this.scalarSchema = scalarSchema;
		try {
			Store s = Store.getInstance();
			Double storedValue = s.get(scalarSchema.getKey());
			if (storedValue == null) {
				this.value = value;
			} else {
				this.value = storedValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.value = value;
		}
	}
	
	public synchronized void setValueFromUser(double requestedValue) {
		double fromDisplay = getScalar().getBounds().toDisplay().invert(requestedValue);
		setValue(fromDisplay);
	}
		
	public synchronized void setValue(double requestedValue) {
		double constrainedValue = constrain(requestedValue);
		timestamp = System.currentTimeMillis();
		this.value = constrainedValue;
		notifyListeners();
		org.singinst.uf.common.LogUtil.info("storing with key \"" + scalarSchema.getKey() + "\" and value of "+value);
		try {
			Store s = Store.getInstance();
			s.put(scalarSchema.getKey(), value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyListeners() {
		for (ValueListener valueListener : valueListeners) {
			valueListener.fireUpdate(value);
		}
	}

	private double constrain(double requestedValue) {
		double rounded = MathUtil.round(requestedValue, ScalarSchema.getMaxDecimalDigits());
		LineBounds boundsConstraint = getScalar().getBoundsConstraint();
		if (boundsConstraint != null) {
			return boundsConstraint.constrain(rounded);
		} else {
			return rounded;
		}
	}

	public void addUpdateListener(ValueListener valueListener) {
		valueListeners.add(valueListener);
		valueListener.fireUpdate(getCachedValue());
	}

	public void unshiftUpdateListener(ValueListener valueListener) {
		valueListeners.add(0, valueListener);
		valueListener.fireUpdate(getCachedValue());
	}

	public static ScalarValueHolder findById(ScalarSchema scalarSchema) {
		String key = scalarSchema.getKey();
		ScalarValueHolder retVal = store.get(key);
		if (retVal == null) {
			LineBounds bounds = scalarSchema.getLineBounds();
			retVal = new ScalarValueHolder(scalarSchema, bounds.getMidpoint());
			store.put(key, retVal);
		}
		return retVal;
	}

	public synchronized double getValue() {
		if (calculation != null && needsCalc(new NeedsCalcCache())) {
			calculation.getHtmlConsole().setLength(0);
			setValue(calculation.evaluate());
		}
		return value;
	}

	public synchronized double getCachedValue() {
		return value;
	}

	private static String constructKey(String namespace, String name) {
		return namespace + "." + name;
	}

	public static ScalarValueHolder findById(String namespace, String name) {
		String key = constructKey(namespace, name);
		return findById(key);
	}

	public static ScalarValueHolder findById(String key) {
		ScalarValueHolder scalarValueHolder = store.get(key);
		if (scalarValueHolder == null) {
			throw new NullPointerException("Key not found: " + key + "; available keys are: " + store.keySet());
		} else {
			return scalarValueHolder;
		}
	}

	public static Calculation getCalculation(String namespace, String name) {
		return findById(namespace, name).getCalculation();
	}

	public synchronized boolean needsCalc(NeedsCalcCache needsCalcCache) {
		if (getScalar().getDependencies().isEmpty()) {
			return false;
		}
		if (timestamp == 0) {
			return true;
		}
		
		Set<ScalarValueHolder> otherConjectures = new HashSet<ScalarValueHolder>();
		for (NodeMetadata nodeMetadata : getScalar().getDependencies()) {
			otherConjectures.addAll(nodeMetadata.getScalarValueHolders());
//			for (ScalarSchema otherScalar : nodeMetadata.getScalars()) {
//				otherConjectures.add(otherScalar.getScalarValueHolder());
//			}
			for (ScalarRelation relation : nodeMetadata.getScalarRelations()) {
				otherConjectures.addAll(relation.getScalarValues());
			}
		}
		for (ScalarValueHolder otherConjecture : otherConjectures) {
			if (needsCalcCache.needsCalc(otherConjecture) || timestamp < otherConjecture.timestamp) {
				return true;
			}
		}
		return false;
	}

	public void setCalculation(Calculation calculation) {
		this.calculation = calculation;
	}

	public Calculation getCalculation() {
		return calculation;
	}

	public double evaluate(StringBuilder htmlConsole) {
		double retVal = getValue();
		if (calculation != null) {
//			String percentage = MathUtil.round(retVal * 100, 2) + "%"; //FIXME
//			htmlConsole.append(calculation.getDescription() + " = " + percentage + "<br>");
			htmlConsole.append(calculation.getDescription() + " = " + 
					StringUtil.formatProbability(retVal) + "<br>");
		}
		return retVal;
	}

	public ScalarSchema getScalar() {
		return scalarSchema;
	}

	public static void destroyAll() {
		for (ScalarValueHolder holder: store.values()) {
			holder.destroy();
		}
		store.clear();
	}

	private void destroy() {
		valueListeners.clear();
	}
}
