package org.trinity.shell.api;

import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.common.eventbus.EventBus;

public interface ShellDisplayEventDispatcher {
	void dispatchDisplayEvent(final boolean block);

	void registerDisplayEventSource(final EventBus nodeEventBus,
									final DisplayEventSource displayEventSource);

	void unregisterDisplayEventSource(	final EventBus nodeEventBus,
										final DisplayEventSource displayEventSource);
}
