package org.hyperdrive.api.input;

import org.hydrogen.api.display.input.InputModifierName;
import org.hydrogen.api.display.input.Momentum;
import org.hyperdrive.api.core.ManagedDisplay;

public interface KeyBinding {
	void performAction();

	String getKeyName();

	Momentum getMomentum();

	InputModifierName[] getInputModifierNames();

	ManagedDisplay getManagedDisplay();

}
