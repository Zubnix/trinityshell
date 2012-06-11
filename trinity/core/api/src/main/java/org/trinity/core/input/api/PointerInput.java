package org.trinity.core.input.api;

public interface PointerInput extends Input {
	Button getButton();

	InputModifiers getModifiers();

	int getRelativeX();

	int getRelativeY();

	int getRootX();

	int getRootY();
}
