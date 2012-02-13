package org.hypercube.protocol;

import org.hyperdrive.core.ClientWindow;

public class PopupNotify implements ProtocolEventArguments {
	private final ClientWindow transientForClient;

	public PopupNotify(final ClientWindow transientForClient) {
		this.transientForClient = transientForClient;
	}

	public ClientWindow getTransientForClient() {
		return this.transientForClient;
	}
}
