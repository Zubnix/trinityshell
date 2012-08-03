package org.trinity.shell.input.api;

import org.trinity.foundation.input.api.event.KeyNotifyEvent;

public interface KeyInputStringBuilder {

	void build(KeyNotifyEvent input);

	void clearBuffer();

	StringBuffer getStringBuffer();

}
