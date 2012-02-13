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
	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> ProtocolEvent<A> query(
			final ClientWindow client, final T eventType) {
		if (!this.clientEventCache.containsKey(client)) {
			return null;
		}
		return (ProtocolEvent<A>) this.clientEventCache.get(client).get(
				eventType);
	}

	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateCache(
			final ClientWindow client, final ProtocolEvent<A> event) {

		final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents;
		if (this.clientEventCache.containsKey(client)) {
			protocolEvents = this.clientEventCache.get(client);
		} else {
			protocolEvents = new HashMap<ProtocolEventType<?>, ProtocolEvent<?>>();
		}
		updateProtocolEvent(protocolEvents, event, event.getType());
	}

	protected <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateProtocolEvent(
			final Map<ProtocolEventType<?>, ProtocolEvent<?>> protocolEvents,
			final ProtocolEvent<A> event, final T eventType) {

		protocolEvents.put(eventType, event);
	}
}
