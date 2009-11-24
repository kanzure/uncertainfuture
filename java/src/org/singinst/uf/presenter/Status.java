package org.singinst.uf.presenter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Status {
	public static Status singleton = new Status();
	private String value;
	
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	public void setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		support.firePropertyChange(new PropertyChangeEvent(this, "display", oldValue, value));
	}

	public String getValue() {
		return value;
	}

	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public PropertyChangeSupport getSupport() {
		return support;
	}
	
	public void destroy() {
		support = new PropertyChangeSupport(this);
	}
}
