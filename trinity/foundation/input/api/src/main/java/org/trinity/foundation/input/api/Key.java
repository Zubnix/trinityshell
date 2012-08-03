package org.trinity.foundation.input.api;

public class Key {

	private final int keyCode;

	public Key(final int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return this.keyCode;
	}
}
