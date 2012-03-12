package org.hyperdrive.api.input;

import org.hydrogen.api.display.event.KeyNotifyEvent;

public interface ManagedKeyboard extends InputDevice {

	String keyEventToString(KeyNotifyEvent keyNotifyEvent);
}
