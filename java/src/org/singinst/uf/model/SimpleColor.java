package org.singinst.uf.model;

public enum SimpleColor {

	GREEN, BLACK, RED, LIGHT_GRAY;

	public abstract static class Visitor<T> {
		public T visit(SimpleColor color) {
			switch(color) {
				case GREEN: return visit_GREEN();
				case BLACK: return visit_BLACK();
				case RED: return visit_RED();
				case LIGHT_GRAY: return visit_LIGHT_GRAY();
				default: throw new RuntimeException();
			}				
		}
		
		public abstract T visit_GREEN();
		public abstract T visit_BLACK();
		public abstract T visit_RED();
		public abstract T visit_LIGHT_GRAY();
	}
}
