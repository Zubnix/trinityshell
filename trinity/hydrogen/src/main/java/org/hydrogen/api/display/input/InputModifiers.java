package org.hydrogen.api.display.input;

public interface InputModifiers {
	boolean isModifierSet(final Modifier modifier);

	int getInputModifiersMask();
}
