package org.trinity.foundation.input.api;

public class InputModifiers {

	private final int inputModfiersState;

	public InputModifiers(final int inputModifiersState) {
		this.inputModfiersState = inputModifiersState;
	}

	public boolean isModifierSet(final InputModifier modifier) {
		return (this.inputModfiersState & modifier.getMask()) != 0;
	}

	public int getInputModifiersState() {
		return this.inputModfiersState;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof InputModifiers) {
			final InputModifiers otherInputModifiers = (InputModifiers) obj;
			return otherInputModifiers.getInputModifiersState() == getInputModifiersState();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.inputModfiersState;
	}
}
