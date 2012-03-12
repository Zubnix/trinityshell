package org.hydrogen.api.display.input;

public interface MouseInput extends Input {
	Button getButton();

	InputModifiers getModifiers();

	int getRelativeX();

	int getRelativeY();

	int getRootX();

	int getRootY();
}
