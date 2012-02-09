package org.hypercube.protocol;

import org.hydrogen.eventsystem.EventHandlerManager;
import org.hyperdrive.core.ClientWindow;

public interface DesktopProtocol extends EventHandlerManager {
	public void registerClient(ClientWindow client);
}