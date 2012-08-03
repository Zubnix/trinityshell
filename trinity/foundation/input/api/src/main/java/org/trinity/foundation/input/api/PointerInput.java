package org.trinity.foundation.input.api;

public class PointerInput extends Input {

	private final Button button;
	private final InputModifiers inputModifiers;
	private final int relativeX;
	private final int relativeY;
	private final int rootX;
	private final int rootY;

	/*****************************************
	 * 
	 ****************************************/
	public PointerInput(final Momentum momentum,
						final Button button,
						final InputModifiers inputModifiers,
						final int relativeX,
						final int relativeY,
						final int rootX,
						final int rootY) {
		super(momentum);
		this.button = button;
		this.inputModifiers = inputModifiers;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.rootX = rootX;
		this.rootY = rootY;
	}

	public Button getButton() {
		return this.button;
	}

	public InputModifiers getModifiers() {
		return this.inputModifiers;
	}

	public int getRelativeX() {
		return this.relativeX;
	}

	public int getRelativeY() {
		return this.relativeY;
	}

	public int getRootX() {
		return this.rootX;
	}

	public int getRootY() {
		return this.rootY;
	}
}
