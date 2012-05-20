package org.hydrogen.display.api.input;

public interface InputModifiers {
	boolean isModifierSet(final Modifier modifier);

	int getInputModifiersMask();
}
