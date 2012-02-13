package org.hypercube.protocol;

import org.hyperdrive.core.ClientWindow;

public abstract class AbstractDesktopProtocol implements DesktopProtocol {

	private final ProtocolEventCache protocolEventCache;

	public AbstractDesktopProtocol() {
		this.protocolEventCache = new ProtocolEventCache();
	}

	@Override
	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> ProtocolEvent<A> query(
			final ClientWindow client, final T eventType) {
		return this.protocolEventCache.query(client, eventType);
	}

	@Override
	public <A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateProtocolEvent(
			final ClientWindow client, final ProtocolEvent<A> protocolEvent) {
		this.protocolEventCache.updateCache(client, protocolEvent);
		client.fireEvent(protocolEvent);
	}
}
