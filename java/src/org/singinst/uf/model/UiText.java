package org.singinst.uf.model;

public class UiText {

	private final String question;
	private final String help;

	public UiText(String question, String help) {
		this.question = question;
		this.help = help;
	}

	public String getHelp() {
		return help;
	}

	public String getQuestion() {
		return question;
	}
}