package org.trinity.shell.api.input;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;

public interface ShellKeyInputStringBuilder {

	void append(String string);

	void append(KeyNotifyEvent input);

	void clear();
}