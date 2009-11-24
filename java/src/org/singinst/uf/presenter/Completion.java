package org.singinst.uf.presenter;

import java.util.ArrayList;
import java.util.List;

import org.singinst.uf.model.ValueListener;

public class Completion {
	private static Completion singleton = new Completion();
	private String display;
	private int numStages;
	private int completed;
	private List<ValueListener> listeners = new ArrayList<ValueListener>();
	
	public static Completion getInstance() {
		return singleton;
	}
	
	public synchronized boolean init(String display, int numStages) {
		if (completed < this.numStages) {
			return false;
		
		}
		this.display = display;
		this.numStages = numStages;
		completed = 0;
		fireUpdate();
		return true;
	}

	private void fireUpdate() {
		for (ValueListener valueListener : listeners) {
			valueListener.fireUpdate(0);
		}
	}

	public synchronized void tick() {
		completed++;
		fireUpdate();
	}

	public synchronized int getCompleted() {
		return completed;
	}

	public synchronized int getNumStages() {
		return numStages;
	}

	public void addUpdateListener(ValueListener valueListener) {
		listeners.add(valueListener);
	}

	public String getDisplay() {
		return display;
	}
	
	public void destroy() {
		listeners.clear();
	}
}
