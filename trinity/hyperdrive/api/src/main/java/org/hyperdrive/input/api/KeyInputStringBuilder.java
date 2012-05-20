package org.hyperdrive.input.api;

import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hyperdrive.foundation.api.ManagedDisplay;

public interface KeyInputStringBuilder {

	void build(KeyNotifyEvent input);

	void clearBuffer();

	ManagedDisplay getManagedDisplay();

}
