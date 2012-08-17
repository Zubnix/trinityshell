package org.trinity.shell.input.api;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;

public interface KeyInputStringBuilder {

	void append(String string);

	void append(KeyNotifyEvent input);

	void clear();
}