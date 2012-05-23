package org.trinity.core.input.api;

public interface InputModifiers {
	boolean isModifierSet(final Modifier modifier);

	int getInputModifiersMask();
}
