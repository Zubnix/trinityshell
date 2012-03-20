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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XPropertyXAtomAtoms;
import org.fusion.x11.core.XPropertyXAtomMultiText;
import org.fusion.x11.core.XPropertyXAtomSingleText;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.event.XClientMessageEvent;
import org.fusion.x11.ewmh.EwmhAtoms;
import org.fusion.x11.ewmh._NetWmIcon;
import org.fusion.x11.ewmh._NetWmIconInstance;
import org.fusion.x11.icccm.Icccm;
import org.fusion.x11.icccm.IcccmAtoms;
import org.fusion.x11.icccm.WmHints;
import org.fusion.x11.icccm.WmHintsInstance;
import org.fusion.x11.icccm.WmNormalHints;
import org.fusion.x11.icccm.WmSizeHintsInstance;
import org.fusion.x11.icccm.WmStateEnum;
import org.fusion.x11.icccm.WmStateInstance;
import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.PropertyInstance;
import org.hydrogen.api.event.EventHandler;
import org.hydrogen.display.PropertyInstanceText;
import org.hydrogen.display.PropertyInstanceTexts;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.geo.GeoEvent;
import org.hyperdrive.api.geo.GeoOperation;
import org.hyperdrive.core.BasePropertyManipulator;
import org.hyperdrive.core.BasePropertyChangedEvent;
import org.hyperdrive.protocol.AbstractDesktopProtocol;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XDesktopProtocol extends AbstractDesktopProtocol {

	private static final Logger LOGGER = Logger
			.getLogger(XDesktopProtocol.class);

	private static final String ERROR_PROP_MANIP_NOT_FOUND = "Properties manipulator for client %s not found. Did you register the client with the protocol?";

	private abstract class PropertyListener<P extends PropertyInstance, T extends XPropertyXAtom<P>>
			implements EventHandler<BasePropertyChangedEvent<T>> {

		@Override
		public void handleEvent(final BasePropertyChangedEvent<T> event) {

			final RenderArea client = event.getRenderArea();
			final PlatformRenderArea platformRenderArea = client
					.getPlatformRenderArea();
			final T changedProperty = event.getChangedProperty();

			final P propertyInstance = platformRenderArea
					.getPropertyInstance(changedProperty);

			handlePropertyInstance(client, propertyInstance);
		}

		abstract void handlePropertyInstance(RenderArea client,
				P propertyInstance);
	}

	private final class WmHintsListener extends
			PropertyListener<WmHintsInstance, WmHints> {

		@Override
		void handlePropertyInstance(final RenderArea client,
				final WmHintsInstance propertyInstance) {
			XDesktopProtocol.this.wmHintsInterpreter.handleWmHint(client,
					propertyInstance);
		}
	}

	private final class WmNormalHintsListener extends
			PropertyListener<WmSizeHintsInstance, WmNormalHints> {

		@Override
		void handlePropertyInstance(final RenderArea client,
				final WmSizeHintsInstance propertyInstance) {
			XDesktopProtocol.this.wmNormalHintsInterpreter.handleWmNormalHints(
					client, propertyInstance);
		}
	}

	private final class WmNameListener extends
			PropertyListener<PropertyInstanceText, XPropertyXAtomSingleText> {

		@Override
		void handlePropertyInstance(final RenderArea client,
				final PropertyInstanceText propertyInstance) {
			XDesktopProtocol.this.wmNameInterpreter.handleWmName(client,
					propertyInstance);
		}
	}

	private final class WmClassListener extends
			PropertyListener<PropertyInstanceTexts, XPropertyXAtomMultiText> {
		@Override
		void handlePropertyInstance(final RenderArea client,
				final PropertyInstanceTexts propertyInstance) {
			XDesktopProtocol.this.wmClassInterpreter.handleWmClass(client,
					propertyInstance);
		}
	}

	private final class ClientVisibilityListener implements
			EventHandler<GeoEvent> {
		@Override
		public void handleEvent(final GeoEvent event) {
			final RenderArea client = (RenderArea) event
					.getGeoTransformableRectangle();
			final boolean visible = event.getGeoTransformation().isVisible1();
			updateWmState(client, visible);
		}
	}

	private final class NetWmIconListener extends
			PropertyListener<_NetWmIconInstance, _NetWmIcon> {
		@Override
		void handlePropertyInstance(final RenderArea client,
				final _NetWmIconInstance propertyInstance) {
			XDesktopProtocol.this.netWmIconInterpreter.handleWmIcon(client,
					propertyInstance);
		}
	}

	private final Icccm icccm;
	private final InputPreferenceParser inputPreferenceParser;

	private final XDisplay display;

	// icccm
	private final WmNormalHintsInterpreter wmNormalHintsInterpreter;
	private final WmHintsInterpreter wmHintsInterpreter;
	private final WmNameInterpreter wmNameInterpreter;
	private final WmClassInterpreter wmClassInterpreter;

	// ewmh
	private final NetWmIconInterpreter netWmIconInterpreter;

	private final Map<RenderArea, BasePropertyManipulator> clientPropertiesManipulators;

	static final String EMPTY_STRING = "";

	public XDesktopProtocol(final ManagedDisplay managedDisplay) {
		this.inputPreferenceParser = new InputPreferenceParser();
		this.clientPropertiesManipulators = new HashMap<RenderArea, BasePropertyManipulator>();

		this.display = (XDisplay) managedDisplay.getDisplay();

		this.icccm = new Icccm(this.display);

		this.wmNormalHintsInterpreter = new WmNormalHintsInterpreter(this);
		this.wmHintsInterpreter = new WmHintsInterpreter(this);
		this.wmNameInterpreter = new WmNameInterpreter(this);
		this.wmClassInterpreter = new WmClassInterpreter(this);

		this.netWmIconInterpreter = new NetWmIconInterpreter(this);

		if (this.icccm.getSelectionManager().isScreenSelectionAvailable()) {
			this.icccm.getSelectionManager().initScreenSelection();
		} else {
			// TODO handle screen nro's. For now default to 0
			throw new Error(
					String.format(
							"No screen selection available! Is there a window manager running for screen %d?",
							0));
		}
	}

	@Override
	public void registerClient(final RenderArea client) {
		final BasePropertyManipulator propertiesManipulator = new BasePropertyManipulator(
				client);
		this.clientPropertiesManipulators.put(client, propertiesManipulator);
		readProtocolProperties(client, propertiesManipulator);
		installListeners(client);
		((XWindow) client.getPlatformRenderArea()).addToSaveSet();
	}

	@Override
	public boolean requestDelete(final RenderArea client) {
		final XPropertyInstanceXAtoms wmProtocolsInstance = this.clientPropertiesManipulators
				.get(client)
				.getPropertyValue(IcccmAtoms.WM_PROTOCOLS_ATOM_NAME);
		final XAtom wmDeleteWindow = this.icccm.getIcccmAtoms()
				.getWmDeleteWindow();

		boolean wmDelete = false;
		if (wmDeleteWindow != null) {
			for (final XAtom xAtom : wmProtocolsInstance.getAtoms()) {
				wmDelete = xAtom.equals(wmDeleteWindow);
				if (wmDelete) {
					break;
				}
			}
		}

		if (wmDelete) {

			final XWindow window = (XWindow) client.getPlatformRenderArea();

			final IntDataContainer intDataContainer = new IntDataContainer(2);
			intDataContainer.writeDataBlock(Integer.valueOf(wmDeleteWindow
					.getAtomId().intValue()));
			intDataContainer.writeDataBlock(Integer
					.valueOf((int) XProtocolConstants.CURRENT_TIME));

			final XPropertyXAtomAtoms wmProtocols = this.icccm.getIcccmAtoms()
					.getWmProtocols();
			final XClientMessageEvent deleteWindowRequest = new XClientMessageEvent(
					window, wmProtocols, intDataContainer);
			window.sendMessage(deleteWindowRequest);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean offerInput(final RenderArea client) {
		// TODO we might want to grab the display and ungrab it when the focus
		// is transferred. This way the user is ensured that when he starts
		// typing after an focus transfer, all the input will be send to the
		// correct client.

		final BasePropertyManipulator propertiesManipulator = this.clientPropertiesManipulators
				.get(client);
		if (propertiesManipulator == null) {
			XDesktopProtocol.LOGGER.error(String.format(
					XDesktopProtocol.ERROR_PROP_MANIP_NOT_FOUND, client));
		}

		final WmHintsInstance wmHintsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_HINTS_ATOM_NAME);
		final XPropertyInstanceXAtoms wmProtocolsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_PROTOCOLS_ATOM_NAME);

		final XWindow window = (XWindow) client.getPlatformRenderArea();
		final InputPreference inputPreference = this.inputPreferenceParser
				.parseInputPreference(window, wmHintsInstance,
						wmProtocolsInstance);
		switch (inputPreference) {
		case GLOBAL_INPUT:
			// Windows with the atom WM_TAKE_FOCUS in their WM_PROTOCOLS
			// property may receive a ClientMessage event from the window
			// manager (as described in section 4.2.8) with WM_TAKE_FOCUS in its
			// data[0] field and a valid timestamp (i.e. not CurrentTime ) in
			// its data[1] field.

			final XWindow screenSelectionOwner = this.icccm
					.getSelectionManager().getIcccmSelectionOwner();
			final XAtom wmProtocolsAtom = this.icccm.getIcccmAtoms()
					.getWmProtocols();

			// construct clientmessage data
			final IntDataContainer clientMessageData = new IntDataContainer(2);
			final XAtom wmTakeFocus = this.icccm.getIcccmAtoms()
					.getWmTakeFocus();
			final Integer wmTakeFocusId = Integer.valueOf(wmTakeFocus
					.getAtomId().intValue());
			final Integer time = Integer.valueOf(this.display
					.getLastServerTime());
			clientMessageData.writeDataBlock(wmTakeFocusId);
			clientMessageData.writeDataBlock(time);

			final XClientMessageEvent setInputFocusMessage = new XClientMessageEvent(
					screenSelectionOwner, wmProtocolsAtom, clientMessageData);
			window.sendMessage(setInputFocusMessage);

			break;
		case LOCAL_INPUT:
			if (!client.hasInputFocus()) {
				client.setInputFocus();
			}
			break;
		case NO_INPUT:
			// Don't give any input
			break;
		case PASSIVE_INPUT:
			client.setInputFocus();
			break;
		}

		return true;
	}

	protected void updateWmState(final RenderArea client, final boolean visible) {
		final WmStateEnum state = visible ? WmStateEnum.NormalState
				: WmStateEnum.WithdrawnState;
		final XWindow iconWindow = this.display.getNoneWindow();
		this.clientPropertiesManipulators.get(client).setPropertyValue(
				IcccmAtoms.WM_STATE_ATOM_NAME,
				new WmStateInstance(this.display, state, iconWindow));
	}

	protected void readProtocolProperties(final RenderArea client,
			final BasePropertyManipulator propertiesManipulator) {

		final WmHintsInstance wmHintsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_HINTS_ATOM_NAME);
		final WmSizeHintsInstance wmSizeHintsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_NORMAL_HINTS_ATOM_NAME);
		final PropertyInstanceText wmNameInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_NAME_ATOM_NAME);
		final PropertyInstanceTexts wmClassInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_CLASS_ATOM_NAME);
		final _NetWmIconInstance wmIconInstance = propertiesManipulator
				.getPropertyValue(EwmhAtoms.NET_WM_ICON_ATOM_NAME);

		// update all protocol events for the client so they can be queried
		// in the future

		this.wmHintsInterpreter.handleWmHint(client, wmHintsInstance);
		this.wmNormalHintsInterpreter.handleWmNormalHints(client,
				wmSizeHintsInstance);
		this.wmNameInterpreter.handleWmName(client, wmNameInstance);
		this.netWmIconInterpreter.handleWmIcon(client, wmIconInstance);
		// TODO wm_class
	}

	protected void addClientToSaveSet(final RenderArea client) {
		final XWindow window = (XWindow) client.getPlatformRenderArea();
		window.addToSaveSet();
	}

	protected void installListeners(final RenderArea client) {
		client.addEventHandler(new WmHintsListener(),
				BasePropertyChangedEvent
						.TYPE(IcccmAtoms.WM_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNormalHintsListener(),
				BasePropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NORMAL_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNameListener(),
				BasePropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NAME_ATOM_NAME));
		client.addEventHandler(new WmClassListener(),
				BasePropertyChangedEvent
						.TYPE(IcccmAtoms.WM_CLASS_ATOM_NAME));
		client.addEventHandler(new ClientVisibilityListener(),
				GeoOperation.VISIBILITY);
		client.addEventHandler(new NetWmIconListener(),
				BasePropertyChangedEvent
						.TYPE(EwmhAtoms.NET_WM_ICON_ATOM_NAME));
	}
}
