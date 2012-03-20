package org.hyperdrive.api.input;

import org.hydrogen.api.event.EventManager;

public interface InputDevice {
	void grab();

	void release();

	boolean isGrabbed();

	void addInputEventManager(EventManager inputEventManager);

	void removeInputEventManager(EventManager inputEventManager);
}
