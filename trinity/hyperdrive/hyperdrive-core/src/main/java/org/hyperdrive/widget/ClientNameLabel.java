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
import org.hydrogen.paintinterface.PaintCall;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.protocol.ClientWindowDescriptionNotify;
import org.hyperdrive.protocol.DesktopProtocol;
import org.hyperdrive.protocol.ProtocolEvent;

// TODO documentation
/**
 * 
 * A <code>ClientNameLabel</code> shows a text property from a
 * {@link ClientWindow}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ClientNameLabel extends Widget {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static interface ClientNameLabelView extends View {
		PaintCall<?, ?> onNameUpdate(String name, Object... args);
	}

	private ClientWindow targetWindow;
	private final DesktopProtocol desktopProtocol;
	private String namePropertyName;

	/**
	 * 
	 * @param namePropertyName
	 */
	public ClientNameLabel(final DesktopProtocol desktopProtocol) {
		super();
		this.desktopProtocol = desktopProtocol;
	}

	/**
	 * 
	 * @return
	 */
	public String getNamePropertyName() {
		return this.namePropertyName;
	}

	/**
	 * 
	 * @param namePropertyName
	 */
	public void setNamePropertyName(final String namePropertyName) {
		this.namePropertyName = namePropertyName;
	}

	@Override
	protected ClientNameLabelView initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newClientNameLabelView();
	}

	@Override
	public ClientNameLabelView getView() {
		return (ClientNameLabelView) super.getView();
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

		targetWindow
				.addEventHandler(
						new EventHandler<ProtocolEvent<ClientWindowDescriptionNotify>>() {
							@Override
							public void handleEvent(
									final ProtocolEvent<ClientWindowDescriptionNotify> event) {
								final String name = event.getEventArguments()
										.getName();
								updateNameLabel(name);
							}
						}, ProtocolEvent.DESCRIPTION_NOTIFY);

		// TODO automatically call painter when we call a method of the view?
		final String clientName = this.desktopProtocol
				.query(getTargetWindow(), ProtocolEvent.DESCRIPTION_NOTIFY)
				.getEventArguments().getName();
		updateNameLabel(clientName);
	}

	/**
	 * 
	 * 
	 */
	protected void updateNameLabel(final String name) {
		getPainter().paint(getView().onNameUpdate(name));
	}

	/**
	 * 
	 * @return
	 */
	public ClientWindow getTargetWindow() {
		return this.targetWindow;
	}
}
