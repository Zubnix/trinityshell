package org.hydrogen.display.api.input;

public interface KeyboardInput extends Input {
	Key getKey();

	InputModifiers getModifiers();
}
