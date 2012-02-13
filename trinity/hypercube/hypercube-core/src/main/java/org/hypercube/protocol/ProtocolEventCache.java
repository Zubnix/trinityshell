package org.hypercube.protocol;

import java.util.HashMap;
import java.util.Map;

import org.hyperdrive.core.ClientWindow;

public class ProtocolEventCache {
	private final Map<ClientWindow, Map<ProtocolEventType<?>, ProtocolEvent<?>>> clientEventCache;

	public ProtocolEventCache() {
		this.clientEventCache = new HashMap<ClientWindow, Map<ProtocolEventType<?>, ProtocolEvent<?>>>();
	}

	@SuppressWarnings("unchecked")
	public <T extends ProtocolEventType<? extends ProtocolEventArguments>> ProtocolEvent<? extends T> query(
			final ClientWindow client, final T eventType) {

		return (ProtocolEvent<? extends T>) this.clientEventCache.get(client)
				.get(eventType);
	}

	public <T extends ProtocolEventType<? extends ProtocolEventArguments>> void upateCache(
			final ClientWindow client, final ProtocolEvent<? extends T> event) {

		final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents;
		if (this.clientEventCache.containsKey(client)) {
			protocolEvents = this.clientEventCache.get(client);
		} else {
			protocolEvents = new HashMap<ProtocolEventType<?>, ProtocolEvent<?>>();
		}
		updateProtocolEvent(protocolEvents, event, event.getType());
	}

	protected <T extends ProtocolEventType<? extends ProtocolEventArguments>> void updateProtocolEvent(
			final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents,
			final ProtocolEvent<? extends T> event, final T eventType) {

		protocolEvents.put(eventType, event);
	}
}
