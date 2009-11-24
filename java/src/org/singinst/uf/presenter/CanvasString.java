package org.singinst.uf.presenter;

public class CanvasString {

	private final String main;
	private final String optionalPower;

	public CanvasString(String main, String optionalPower) {
		this.main = main;
		this.optionalPower = optionalPower;
		
	}

	public CanvasString(String main) {
		this(main, null);
		
	}

	public String getMain() {
		return main;
	}

	public String getOptionalPower() {
		return optionalPower;
	}

}
