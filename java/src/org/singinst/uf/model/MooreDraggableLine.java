package org.singinst.uf.model;

import org.singinst.uf.math.SimplePoint;
import org.singinst.uf.presenter.DraggableLine;
import org.singinst.uf.presenter.SimpleStyle;

public class MooreDraggableLine extends DraggableLine {

	private final MooreAsymptote mooreAsymptote;

	public MooreDraggableLine(MooreAsymptote mooreAsymptote, NotablePercentile percentile) {
		super(percentile.getPriority(), new SimpleStyle(percentile.getColor(), true));
		this.mooreAsymptote = mooreAsymptote;
	}

	@Override
	public void dragTo(SimplePoint point) {
		mooreAsymptote.setY(point.y);
	}

}
