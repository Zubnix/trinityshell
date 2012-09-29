package org.trinity.foundation.input.api;

public class KeyboardInput extends Input {

	private final Key key;
	private final InputModifiers inputModifiers;

	public KeyboardInput(final Momentum momentum, final Key key, final InputModifiers inputModifiers) {
		super(momentum);
		this.key = key;
		this.inputModifiers = inputModifiers;
	}

	public Key getKey() {
		return this.key;
	}

	public InputModifiers getInputModifiers() {
		return this.inputModifiers;
	}
}
