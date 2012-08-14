package org.trinity.foundation.input.api;

public class Key {

	private final int keyCode;

	public Key(final int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Key) {
			final Key otherKey = (Key) obj;
			return getKeyCode() == otherKey.getKeyCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getKeyCode();
	}
}
