package org.singinst.uf.view;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.singinst.uf.model.ScalarValueHolder;
import org.singinst.uf.model.ValueListener;

public class RangeScalarBinder {

	public void bind(final ScalarValueHolder scalarValueHolder, final BoundedRangeModel model) {
		scalarValueHolder.addUpdateListener(new ValueListener() {
			public void fireUpdate(double value) {
				model.setValue((int)value);
			}
		});
		
		model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scalarValueHolder.setValue((double) model.getValue());
			}
			
		});

	}

}
