package org.trinity.shell.api.surface;

import org.trinity.foundation.display.api.event.DisplayEventSource;

import com.google.common.eventbus.EventBus;

public interface ShellDisplayEventDispatcher {
	void dispatchDisplayEvent(final boolean block);

	void registerDisplayEventSourceListener(final EventBus nodeEventBus,
											final DisplayEventSource displayEventSource);

	void unregisterDisplayEventSourceListener(	EventBus nodeEventBus,
												DisplayEventSource displayEventSource);

	void unregisterAllDisplayEventSourceListeners(DisplayEventSource displayEventSource);
}