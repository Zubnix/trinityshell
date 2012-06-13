package org.trinity.foundation.input.api;

public interface InputModifiers {
	boolean isModifierSet(final Modifier modifier);

	int getInputModifiersMask();
}
