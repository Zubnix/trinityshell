package org.trinity.core.input.api;

public interface MouseInput extends Input {
	Button getButton();

	InputModifiers getModifiers();

	int getRelativeX();

	int getRelativeY();

	int getRootX();

	int getRootY();
}
