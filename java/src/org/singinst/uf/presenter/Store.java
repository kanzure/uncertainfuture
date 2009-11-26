package org.singinst.uf.presenter;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.singinst.uf.model.ScalarValueHolder;

public abstract class Store {
	private static Store instance = null;
	
	public abstract Double get(String key);
	public abstract void put(String key, double value);
	
	public void loadData(String jsonSubset) {
		try {
			attemptLoad(jsonSubset);
		} catch (Exception e) {
			Status.singleton.setValue("<html>" + HtmlUtil.red("Load failed!"));
			e.printStackTrace();
		}
	}
	
	/**
	 * example: {"caption": "loaded foo", "Q2-3.value": "1.35"}
	 */
	private void attemptLoad(String jsonSubset) {
		Map<String, String> map = new TreeMap<String, String>();
		
		String[] properties = jsonSubset.replaceFirst("\\{(.+)\\}", "$1").split("\\,");
		for (String property : properties) {
			String[] keyValue = property.split("\\:");
			String key = scrub(keyValue[0]);
			String value = scrub(keyValue[1]);
			map.put(key, value);
		}
		String caption = map.remove("caption");
		if (caption == null) {
			caption = "loaded: " + map.toString();
		}
		for (Entry<String, String> e: map.entrySet()) {
			
			ScalarValueHolder.findById(e.getKey()).setValue(Double.valueOf(e.getValue()));
		}
		Status.singleton.setValue(caption);
	}
	
	private String scrub(String string) {
		return string.trim().replaceFirst("\\\"(.+)\\\"", "$1").replaceFirst("\\'(.+)\\'", "$1");
	}
	
	public static void setInstance(Store instance) {
		Store.instance = instance;
	}
	public static Store getInstance() {
		return instance;
	}
}
