package org.hyperdrive.api.input;

import org.hydrogen.api.geometry.Coordinates;

public interface ManagedMouse extends InputDevice {
	Coordinates getAbsolutePosition();

	void refreshPositionInfo();
}
