package org.trinity.foundation.input.api;

public class InputModifiers {

	private final int inputModfiersMask;

	/*****************************************
	 * 
	 ****************************************/
	public InputModifiers(final int inputModifiersMask) {
		this.inputModfiersMask = inputModifiersMask;
	}

	public boolean isModifierSet(final Modifier modifier) {
		return (this.inputModfiersMask & modifier.getModifierMask()) != 0;
	}

	public int getInputModifiersMask() {
		return this.inputModfiersMask;
	}
}
