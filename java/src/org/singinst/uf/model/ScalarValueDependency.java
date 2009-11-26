package org.singinst.uf.model;

import java.util.HashSet;
import java.util.Set;

public class ScalarValueDependency {
	private boolean validityChecked = false;
	private Set<ScalarValueHolder> dependencies = new HashSet<ScalarValueHolder>();
	
	public void validate() {
		if (!validityChecked) {
			validityChecked = true;
		}
	}
	
	public double value(String namespace, String name) {
		ScalarValueHolder scalarValueHolder = ScalarValueHolder.findById(namespace, name);
		if (!validityChecked) {
			dependencies.add(scalarValueHolder);
		}
		return scalarValueHolder.getValue();
	}

}
