package org.singinst.uf.presenter;

import org.singinst.uf.math.SimplePoint;

public interface MouseDragListener {
	int mouseDown(SimplePoint point);
	void dragTo(SimplePoint point);
}
