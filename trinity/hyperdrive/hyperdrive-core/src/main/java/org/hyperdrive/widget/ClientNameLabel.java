/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import org.hydrogen.eventsystem.EventHandler;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.protocol.ClientWindowDescription;
import org.hyperdrive.protocol.DesktopProtocol;

// TODO documentation
//TODO split into label & client name label
/**
 * 
 * A <code>ClientNameLabel</code> shows a text property from a
 * {@link ClientWindow}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ClientNameLabel extends Label {

	@ViewDefinition
	public interface View extends Label.View {

	}

	private ClientWindow targetWindow;
	private final DesktopProtocol desktopProtocol;

	/**
	 * 
	 * @param namePropertyName
	 */
	public ClientNameLabel(final DesktopProtocol desktopProtocol) {
		this.desktopProtocol = desktopProtocol;
	}

	/**
	 * 
	 * @param targetWindow
	 * 
	 */
	public void setTargetWindow(final ClientWindow targetWindow) {
		if (this.targetWindow != null) {
			// TODO remove listener from previous window
		}
		this.targetWindow = targetWindow;

		targetWindow.addEventHandler(
				new EventHandler<ClientWindowDescription>() {
					@Override
					public void handleEvent(final ClientWindowDescription event) {
						final String name = event.getName();
						updateNameLabel(name);
					}
				}, ClientWindowDescription.TYPE);

		final ClientWindowDescription description = (ClientWindowDescription) this.desktopProtocol
				.query(getTargetWindow(), ClientWindowDescription.TYPE);
		if (description != null) {
			final String clientName = description.getName();
			updateNameLabel(clientName);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void updateNameLabel(final String name) {
		getView().onTextUpdate(name);
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow getTargetWindow() {
		return this.targetWindow;
	}
}
