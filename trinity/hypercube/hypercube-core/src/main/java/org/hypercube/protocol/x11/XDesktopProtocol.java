package org.hypercube.protocol.x11;

import org.hydrogen.eventsystem.EventBus;
import org.hypercube.protocol.DesktopProtocol;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.ManagedDisplay;

public class XDesktopProtocol extends EventBus implements DesktopProtocol {

	private final ManagedDisplay managedDisplay;

	private final IcccmProtocol icccmProtocol;

	public XDesktopProtocol(final ManagedDisplay managedDisplay) {
		this.managedDisplay = managedDisplay;
		this.icccmProtocol = new IcccmProtocol(this);
	}

	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	public IcccmProtocol getIcccmProtocol() {
		return this.icccmProtocol;
	}

	@Override
	public void registerClient(final ClientWindow client) {
		getIcccmProtocol().handleClient(client);
	}
}
