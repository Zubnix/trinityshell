package org.hyperdrive.input.api;

import org.hyperdrive.foundation.api.ManagedDisplay;
import org.trinity.core.display.api.event.KeyNotifyEvent;

public interface KeyInputStringBuilder {

	void build(KeyNotifyEvent input);

	void clearBuffer();

	ManagedDisplay getManagedDisplay();

}
