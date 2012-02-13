package org.hypercube.protocol;

import org.hyperdrive.core.ClientWindow;

public interface DesktopProtocol {

	void registerClient(ClientWindow client);

	/**
	 * 
	 * @param client
	 * @return true if the underlying protocol implements this call.
	 */
	boolean offerInput(ClientWindow client);

	/**
	 * 
	 * @param client
	 * @return true if the underlying protocol implements this call.
	 */
	boolean requestDelete(ClientWindow client);

	<A extends ProtocolEventArguments, T extends ProtocolEventType<A>> ProtocolEvent<A> query(
			ClientWindow client, T eventType);

	<A extends ProtocolEventArguments, T extends ProtocolEventType<A>> void updateProtocolEvent(
			ClientWindow client, ProtocolEvent<A> protocolEvent);
}