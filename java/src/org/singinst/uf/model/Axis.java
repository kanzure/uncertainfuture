package org.singinst.uf.model;

import org.singinst.uf.presenter.LineBounded;
import org.singinst.uf.presenter.LineBounds;

public class Axis implements LineBounded {

	private final LineBounds lineBounds;
	private final boolean visibleAxis;

	public Axis(LineBounds lineBounds) {
		this(lineBounds, true);
	}
	
	public Axis(LineBounds lineBounds, boolean visibleAxis) {
		this.lineBounds = lineBounds;
		this.visibleAxis = visibleAxis;
	}

	public LineBounds getLineBounds() {
		return lineBounds;
	}

	public boolean isVisibleAxis() {
		return visibleAxis;
	}

}
