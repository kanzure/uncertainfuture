package org.singinst.uf.model;

import org.singinst.uf.math.SimplePoint;

public class IncidentLine extends PercentileDraggableLine {

	private final HazardRateNodeMetadataContentsFactory hazardRateNodeMetadataContentsFactory;

	public IncidentLine(HazardRateNodeMetadataContentsFactory hazardRateNodeMetadataContentsFactory, NotablePercentile percentile) {
		super(percentile);
		this.hazardRateNodeMetadataContentsFactory = hazardRateNodeMetadataContentsFactory;
	}
	
	@Override
	public void dragTo(SimplePoint point) {
		hazardRateNodeMetadataContentsFactory.dragTo(getPercentile(), point);
	}

}
