package org.trinity.shell.core.api;

import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.common.eventbus.EventBus;

public interface ShellDisplayEventDispatcher {
	void dispatchDisplayEvent(final boolean block);

	void registerDisplayEventSource(final EventBus eventBus,
									final DisplayEventSource forDisplayEventSource);
}
