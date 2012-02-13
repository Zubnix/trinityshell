package org.hypercube.protocol;

import org.hydrogen.eventsystem.EventHandlerManager;
import org.hyperdrive.core.ClientWindow;

public interface DesktopProtocol extends EventHandlerManager {

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

	<T extends ProtocolEventType<? extends ProtocolEventArguments>> ProtocolEvent<? extends T> query(
			ClientWindow client, T eventType);

	<T extends ProtocolEventType<? extends ProtocolEventArguments>> void updateProtocolEvent(
			ClientWindow client, ProtocolEvent<? extends T> protocolEvent);
}