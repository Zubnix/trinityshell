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

import java.util.LinkedList;
import java.util.List;

import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.event.FocusNotifyEvent;
import org.hydrogen.displayinterface.input.Momentum;
import org.hydrogen.eventsystem.EventHandler;
import org.hydrogen.paintinterface.PaintCall;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.geo.GeoEvent;
import org.hyperdrive.geo.GeoManagerLine;
import org.hyperdrive.geo.LineProperty;
import org.hyperdrive.protocol.DesktopProtocol;

// TODO documentation
// TODO implement as pager?
/**
 * A <code>ClientManager</code> provides an overview of {@link ClientWindow}s.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ClientManager extends Widget {

	// TODO documentation
	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static class ClientManagerLabel extends ClientNameLabel {
		public static interface ClientManagerLabelView extends
				ClientNameLabelView {
			PaintCall<?, ?> onClientGainFocus();

			PaintCall<?, ?> onClientLostFocus();
		}

		private final EventHandler<ButtonNotifyEvent> eventHandler;

		/**
		 * 
		 * @param namePropertyName
		 */
		public ClientManagerLabel(final DesktopProtocol desktopProtocol) {
			super(desktopProtocol);
			this.eventHandler = new EventHandler<ButtonNotifyEvent>() {
				@Override
				public void handleEvent(final ButtonNotifyEvent event) {
					if (event.getInput().getMomentum() == Momentum.STARTED) {
						if (getTargetWindow().isVisible()) {
							getTargetWindow().requestRaise();
							getTargetWindow().getPlatformRenderArea()
									.setInputFocus();
						} else {
							getTargetWindow().setVisibility(true);
							getTargetWindow().requestVisibilityChange();
							getTargetWindow().requestRaise();
							getTargetWindow().getPlatformRenderArea()
									.setInputFocus();
						}
					}
				}
			};
		}

		@Override
		public void setTargetWindow(final ClientWindow targetWindow) {
			super.setTargetWindow(targetWindow);
			manageClient();
		}

		/**
		 * 
		 */
		protected void manageClient() {
			addEventHandler(this.eventHandler, ButtonNotifyEvent.TYPE_PRESSED);
		}

		@Override
		public ClientManagerLabelView getView() {
			return (ClientManagerLabelView) super.getView();
		}

		@Override
		protected ClientManagerLabelView initView(
				final ViewFactory<?> viewFactory) {
			return viewFactory.newClientManagerLabelView();
		}

	}

	// TODO documentation
	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static interface ClientManagerView extends View {
		// TODO more calls that reflect the clientmanger's state
	}

	private final GeoManagerLine geoManagerLine;
	private String namePropertyName;
	private final List<ClientManagerLabel> clientNameLabels;
	private final DesktopProtocol desktopProtocol;

	/**
	 * 
	 * @param namePropertyName
	 */
	public ClientManager(final DesktopProtocol desktopProtocol) {
		this.desktopProtocol = desktopProtocol;
		this.clientNameLabels = new LinkedList<ClientManagerLabel>();
		this.geoManagerLine = new GeoManagerLine(this, true, false);
		setGeoManager(this.geoManagerLine);
		setNamePropertyName(this.namePropertyName);
	}

	/**
	 * 
	 * @param namePropertyName
	 */
	public void setNamePropertyName(final String namePropertyName) {
		this.namePropertyName = namePropertyName;
	}

	/**
	 * 
	 * @return
	 */
	public String getNamePropertyName() {
		return this.namePropertyName;
	}

	@Override
	protected ClientManagerView initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newClientManagerView();
	}

	/**
	 * 
	 * @param client
	 * @return
	 */
	protected ClientManagerLabel newClientNameLabel(final ClientWindow client) {
		final ClientManagerLabel clientManagerLabel = new ClientManagerLabel(
				this.desktopProtocol);
		clientManagerLabel.setTargetWindow(client);
		return clientManagerLabel;
	}

	/**
	 * 
	 * @param clientManagerLabel
	 */
	protected void updateClientManagerLabelVisual(
			final ClientManagerLabel clientManagerLabel) {
		// deactivate all
		for (final ClientManagerLabel otherClientManagerLabel : this.clientNameLabels) {
			otherClientManagerLabel.getPainter().paint(
					otherClientManagerLabel.getView().onClientLostFocus());
		}

		// activate given label
		clientManagerLabel.getPainter().paint(
				clientManagerLabel.getView().onClientGainFocus());
	}

	/**
	 * 
	 * @param client
	 */
	public void manageClient(final ClientWindow client) {
		final ClientManagerLabel clientManagerLabel = newClientNameLabel(client);
		clientManagerLabel.setParent(this);
		clientManagerLabel.setVisibility(true);
		clientManagerLabel.requestVisibilityChange();
		clientManagerLabel.requestReparent();
		clientManagerLabel.setTargetWindow(client);
		this.geoManagerLine.addManagedChild(clientManagerLabel,
				new LineProperty(100));

		// update view when a client is destroyed
		client.addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				ClientManager.this.geoManagerLine
						.removeManagedChild(clientManagerLabel);
				clientManagerLabel.doDestroy();
			}
		}, GeoEvent.DESTROYED);
		this.clientNameLabels.add(clientManagerLabel);

		// update view when a client gets the focus
		client.addEventHandler(new EventHandler<FocusNotifyEvent>() {
			@Override
			public void handleEvent(final FocusNotifyEvent event) {
				updateClientManagerLabelVisual(clientManagerLabel);
			}
		}, FocusNotifyEvent.TYPE_GAIN);
	}
}
