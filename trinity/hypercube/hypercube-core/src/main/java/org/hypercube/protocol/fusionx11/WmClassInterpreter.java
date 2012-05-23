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
import org.hyperdrive.protocol.ClientSoftwareDescription;
import org.trinity.core.display.impl.PropertyInstanceTexts;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class WmClassInterpreter {

	private final XDesktopProtocol xDesktopProtocol;

	WmClassInterpreter(final XDesktopProtocol xDesktopProtocol) {
		this.xDesktopProtocol = xDesktopProtocol;
	}

	void handleWmClass(final RenderArea client,
			final PropertyInstanceTexts propertyInstance) {

		final String instanceName = propertyInstance.getTexts()[0];
		final String className = propertyInstance.getTexts()[1];
		// TODO Update client description

		final ClientSoftwareDescription softwareDescriptionNotify = (ClientSoftwareDescription) this.xDesktopProtocol
				.query(client, ClientSoftwareDescription.TYPE);

		if (softwareDescriptionNotify == null) {
			final ClientSoftwareDescription newSoftwareDescriptionNotify = new ClientSoftwareDescription(
					className, instanceName);

			this.xDesktopProtocol.updateProtocolEvent(client,
					newSoftwareDescriptionNotify);
			return;
		}

		final String oldClassName = softwareDescriptionNotify.getClassName();
		final String oldInstanceName = softwareDescriptionNotify
				.getClientInstanceName();

		final String newClassName;
		if (oldClassName.equals(className)) {
			newClassName = oldClassName;
		} else {
			newClassName = className;
		}

		final String newInstanceName;
		if (oldInstanceName.equals(instanceName)) {
			newInstanceName = oldInstanceName;
		} else {
			newInstanceName = instanceName;
		}

		final ClientSoftwareDescription newSoftwareDescriptionNotify = new ClientSoftwareDescription(
				newClassName, newInstanceName);

		this.xDesktopProtocol.updateProtocolEvent(client,
				newSoftwareDescriptionNotify);
	}
}
