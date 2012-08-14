package org.trinity.display.x11.core.impl;

import org.trinity.foundation.input.api.InputModifier;

public class XInputModifier implements InputModifier {

	private final String name;
	private final int mask;

	public XInputModifier(final int mask, final String name) {
		this.name = name;
		this.mask = mask;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XInputModifier) {
			final XInputModifier otherXInputModifier = (XInputModifier) obj;
			return otherXInputModifier.getMask() == getMask();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.mask;
	}

	public int getMask() {
		return this.mask;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
