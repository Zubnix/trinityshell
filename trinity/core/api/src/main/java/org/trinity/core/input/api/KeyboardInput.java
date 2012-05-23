package org.trinity.core.input.api;

public interface KeyboardInput extends Input {
	Key getKey();

	InputModifiers getModifiers();
}
