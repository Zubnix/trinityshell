/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube.protocol.fusionx11;

import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.protocol.ClientWindowDescriptionNotify;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class WmNameInterpreter {
	private final XDesktopProtocol xDesktopProtocol;

	public WmNameInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmName(final ClientWindow client,
			final PropertyInstanceText propertyInstance) {

		final ClientWindowDescriptionNotify clientWindowDescriptionNotify = (ClientWindowDescriptionNotify) this.xDesktopProtocol
				.query(client, ClientWindowDescriptionNotify.TYPE);

		final String oldDescription;

		if (clientWindowDescriptionNotify == null) {
			oldDescription = XDesktopProtocol.EMPTY_STRING;
		} else {
			oldDescription = clientWindowDescriptionNotify.getDescription();
		}

		final String newName = propertyInstance.getText();
		final ClientWindowDescriptionNotify newWindowDescriptionNotify = new ClientWindowDescriptionNotify(
				newName, oldDescription);

		this.xDesktopProtocol.updateProtocolEvent(client,
				newWindowDescriptionNotify);
	}
}
