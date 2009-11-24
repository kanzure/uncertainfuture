package org.singinst.uf.math;

import java.util.Arrays;

public class SimplePoint {

	public SimplePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public final double x;
	public final double y;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimplePoint) {
			SimplePoint other = (SimplePoint) obj;
			return other.x == x && other.y == y;
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		return canon().hashCode();
	}
	@Override
	public String toString() {
		return canon().toString();
	}
	
	private Object canon() {
		return Arrays.asList(x, y);
	}

	
}
