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

import org.hyperdrive.core.api.RenderArea;
import org.hyperdrive.protocol.ClientWindowDescription;
import org.trinity.core.display.impl.PropertyInstanceText;

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

	void handleWmName(final RenderArea client,
			final PropertyInstanceText propertyInstance) {

		final ClientWindowDescription clientWindowDescription = (ClientWindowDescription) this.xDesktopProtocol
				.query(client, ClientWindowDescription.TYPE);

		final String oldDescription;

		if (clientWindowDescription == null) {
			oldDescription = XDesktopProtocol.EMPTY_STRING;
		} else {
			oldDescription = clientWindowDescription.getDescription();
		}

		final String newName = propertyInstance.getText();
		final ClientWindowDescription newWindowDescriptionNotify = new ClientWindowDescription(
				newName, oldDescription);

		this.xDesktopProtocol.updateProtocolEvent(client,
				newWindowDescriptionNotify);
	}
}
