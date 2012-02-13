package org.hypercube.protocol.x11;

import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hypercube.protocol.ClientWindowDescriptionNotify;
import org.hypercube.protocol.ProtocolEvent;
import org.hyperdrive.core.ClientWindow;

final class WmNameInterpreter {
	private final XDesktopProtocol xDesktopProtocol;

	public WmNameInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmName(final ClientWindow client,
			final PropertyInstanceText propertyInstance) {

		final ProtocolEvent<ClientWindowDescriptionNotify> clientWindowDescriptionNotify = this.xDesktopProtocol
				.query(client, ProtocolEvent.DESCRIPTION_NOTIFY);

		final String oldDescription;

		if (clientWindowDescriptionNotify == null) {
			oldDescription = XDesktopProtocol.EMPTY_STRING;
		} else {
			final ClientWindowDescriptionNotify windowDescriptionNotify = clientWindowDescriptionNotify
					.getEventArguments();
			oldDescription = windowDescriptionNotify.getDescription();
		}

		final String newName = propertyInstance.getText();
		final ClientWindowDescriptionNotify newWindowDescriptionNotify = new ClientWindowDescriptionNotify(
				newName, oldDescription);
		final ProtocolEvent<ClientWindowDescriptionNotify> protocolEvent = new ProtocolEvent<ClientWindowDescriptionNotify>(
				ProtocolEvent.DESCRIPTION_NOTIFY, newWindowDescriptionNotify);

		this.xDesktopProtocol.updateProtocolEvent(client, protocolEvent);
	}
}
