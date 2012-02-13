package org.hypercube.protocol;

import org.hydrogen.eventsystem.EventBus;
import org.hyperdrive.core.ClientWindow;

public abstract class AbstractDesktopProtocol extends EventBus implements
		DesktopProtocol {

	private final ProtocolEventCache protocolEventCache;

	public AbstractDesktopProtocol() {
		this.protocolEventCache = new ProtocolEventCache();
	}

	@Override
	public <T extends ProtocolEventType<? extends ProtocolEventArguments>> ProtocolEvent<? extends T> query(
			final ClientWindow client, final T eventType) {
		return this.protocolEventCache.query(client, eventType);
	}

	@Override
	public <T extends ProtocolEventType<? extends ProtocolEventArguments>> void updateProtocolEvent(
			final ClientWindow client,
			final ProtocolEvent<? extends T> protocolEvent) {
		this.protocolEventCache.upateCache(client, protocolEvent);
		fireEvent(protocolEvent);
	}

}
