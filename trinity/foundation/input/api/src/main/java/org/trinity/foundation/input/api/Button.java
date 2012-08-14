package org.trinity.foundation.input.api;

public class Button {

	private final int buttonCode;

	public Button(final int buttonCode) {
		this.buttonCode = buttonCode;
	}

	public int getButtonCode() {
		return this.buttonCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Button) {
			final Button otherButton = (Button) obj;
			return otherButton.getButtonCode() == getButtonCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getButtonCode();
	}
}
