package org.hypercube.protocol.x11;

import org.fusion.x11.core.XDisplay;
import org.fusion.x11.icccm.Icccm;
import org.fusion.x11.icccm.SelectionManager;
import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.geo.GeoEvent;

public class IcccmProtocol {

	private final Icccm icccm;
	private final XDesktopProtocol xDesktopProtocol;
	private final ManagedDisplay managedDisplay;

	public IcccmProtocol(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
		this.managedDisplay = xDesktopProtocol.getManagedDisplay();
		this.icccm = new Icccm((XDisplay) getManagedDisplay().getDisplay());

		initWmSelection();
	}

	protected void initWmSelection() {
		final SelectionManager selectionManager = this.icccm
				.getSelectionManager();
		final boolean selectionAvailable = selectionManager
				.isScreenSelectionAvailable();
		if (selectionAvailable) {
			selectionManager.initScreenSelection();
		} else {
			// TODO throw a more detailed exception. Another wm is running!
			throw new Error("Another wm is running!");
		}
	}

	public Icccm getIcccm() {
		return this.icccm;
	}

	public ManagedDisplay getManagedDisplay() {
		return this.managedDisplay;
	}

	public void handleClient(final ClientWindow client) {
		// we need to update some X specific window properties when the client
		// state changes. Client preferences are also set here.
		client.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				// TODO update wm_state property

			}
		}, GeoEvent.VISIBILITY);
	}
}
