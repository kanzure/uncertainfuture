package org.singinst.uf.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

import org.singinst.uf.common.LogUtil;
import org.singinst.uf.model.ScalarValueHolder;
import org.singinst.uf.model.ValueListener;

public class TextScalarBinder {
	private boolean userEntry = true;
	
	public void bind(final ScalarValueHolder scalarValueHolder,
			final JFormattedTextField propertyValueDisplay) {
		scalarValueHolder.unshiftUpdateListener(new ValueListener() {
			public void fireUpdate(double value) {
				userEntry = false;
				try {
					String formattedString = scalarValueHolder.getScalar().getLineBounds().userFormat(value);
					propertyValueDisplay.setText(formattedString);
					try {
						propertyValueDisplay.commitEdit();
					} catch (ParseException e) {
						LogUtil.error(e);
					}
				} finally {
					userEntry = true;
				}
			}
		});
		
		propertyValueDisplay.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (userEntry) {
					if ("value".equals(evt.getPropertyName())) {
						scalarValueHolder.setValueFromUser(((Number) evt.getNewValue()).doubleValue());					
					}
				}
			}			
		});
	}

}
