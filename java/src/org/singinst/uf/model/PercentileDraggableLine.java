package org.singinst.uf.model;

import org.singinst.uf.presenter.DraggableLine;
import org.singinst.uf.presenter.SimpleStyle;

public abstract class PercentileDraggableLine extends DraggableLine {

	private final NotablePercentile percentile;

	public PercentileDraggableLine(NotablePercentile percentile) {
		super(percentile.getPriority(), new SimpleStyle(percentile.getColor()));
		this.percentile = percentile;
	}

	public NotablePercentile getPercentile() {
		return percentile;
	}

}
