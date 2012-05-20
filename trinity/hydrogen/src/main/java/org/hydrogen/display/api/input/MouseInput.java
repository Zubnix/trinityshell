package org.hydrogen.display.api.input;

public interface MouseInput extends Input {
	Button getButton();

	InputModifiers getModifiers();

	int getRelativeX();

	int getRelativeY();

	int getRootX();

	int getRootY();
}
