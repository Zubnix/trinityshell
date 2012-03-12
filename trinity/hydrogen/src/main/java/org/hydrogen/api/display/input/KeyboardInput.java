package org.hydrogen.api.display.input;

public interface KeyboardInput extends Input {
	Key getKey();

	InputModifiers getModifiers();
}
