package org.hyperdrive.api.input;

import org.hydrogen.api.event.Type;
import org.hyperdrive.api.core.ManagedDisplay;

public interface InputDevice {
	void grab();

	Type[] getDeviceInputTypes();

	void release();

	boolean isGrabbed();

	ManagedDisplay getManagedDisplay();
}
