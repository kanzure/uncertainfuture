package org.singinst.uf.model;

import java.util.HashMap;
import java.util.Map;

public class NeedsCalcCache {
	
	public Map<ScalarValueHolder, Boolean> cache = new HashMap<ScalarValueHolder, Boolean>();

	public boolean needsCalc(ScalarValueHolder scalarValueHolder) {
		Boolean needsCalc = cache.get(scalarValueHolder);
		if (needsCalc == null) {
			needsCalc = scalarValueHolder.needsCalc(this);
			cache.put(scalarValueHolder, needsCalc);
		}
		return needsCalc;
	}

}
