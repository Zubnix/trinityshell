package org.hypercube.protocol.x11;

import java.util.HashMap;
import java.util.Map;

import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XPropertyXAtomAtoms;
import org.fusion.x11.core.XPropertyXAtomMultiText;
import org.fusion.x11.core.XPropertyXAtomSingleText;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.event.XClientMessageEvent;
import org.fusion.x11.icccm.Icccm;
import org.fusion.x11.icccm.IcccmAtoms;
import org.fusion.x11.icccm.WmHints;
import org.fusion.x11.icccm.WmHintsInstance;
import org.fusion.x11.icccm.WmNormalHints;
import org.fusion.x11.icccm.WmSizeHintsInstance;
import org.fusion.x11.icccm.WmStateEnum;
import org.fusion.x11.icccm.WmStateInstance;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hydrogen.displayinterface.PropertyInstanceTexts;
import org.hydrogen.eventsystem.EventHandler;
import org.hypercube.protocol.AbstractDesktopProtocol;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.core.RenderAreaPropertiesManipulator;
import org.hyperdrive.core.RenderAreaPropertyChangedEvent;
import org.hyperdrive.geo.GeoEvent;

public class XDesktopProtocol extends AbstractDesktopProtocol {

	private abstract class PropertyListener<P extends PropertyInstance, T extends XPropertyXAtom<P>>
			implements EventHandler<RenderAreaPropertyChangedEvent<T>> {

		@Override
		public void handleEvent(final RenderAreaPropertyChangedEvent<T> event) {

			final ClientWindow client = (ClientWindow) event.getRenderArea();
			final PlatformRenderArea platformRenderArea = client
					.getPlatformRenderArea();
			final T changedProperty = event.getChangedProperty();

			final P propertyInstance = platformRenderArea
					.getPropertyInstance(changedProperty);

			handlePropertyInstance(client, propertyInstance);
		}

		abstract void handlePropertyInstance(ClientWindow client,
				P propertyInstance);
	}

	private final class WmHintsListener extends
			PropertyListener<WmHintsInstance, WmHints> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final WmHintsInstance propertyInstance) {
			handleWmHint(client, propertyInstance);
		}
	}

	private final class WmNormalHintsListener extends
			PropertyListener<WmSizeHintsInstance, WmNormalHints> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final WmSizeHintsInstance propertyInstance) {
			handleWmNormalHints(client, propertyInstance);
		}
	}

	private final class WmNameListener extends
			PropertyListener<PropertyInstanceText, XPropertyXAtomSingleText> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final PropertyInstanceText propertyInstance) {
			handleWmName(client, propertyInstance);
		}
	}

	private final class WmClassListener extends
			PropertyListener<PropertyInstanceTexts, XPropertyXAtomMultiText> {
		@Override
		void handlePropertyInstance(final ClientWindow client,
				final PropertyInstanceTexts propertyInstance) {
			handleWmClass(client, propertyInstance);
		}
	}

	private final class ClientVisibilityListener implements
			EventHandler<GeoEvent> {
		@Override
		public void handleEvent(final GeoEvent event) {
			final ClientWindow client = (ClientWindow) event
					.getTransformableSquare();
			final boolean visible = event.getTransformation().isVisible1();
			updateWmState(client, visible);
		}
	}

	private final Icccm icccm;
	private final InputPreferenceParser inputPreferenceParser;

	// private final ManagedDisplay managedDisplay;
	private final XDisplay display;

	private final Map<ClientWindow, RenderAreaPropertiesManipulator> clientPropertiesManipulators;

	public XDesktopProtocol(final ManagedDisplay managedDisplay) {
		this.inputPreferenceParser = new InputPreferenceParser();
		this.clientPropertiesManipulators = new HashMap<ClientWindow, RenderAreaPropertiesManipulator>();
		// this.managedDisplay = managedDisplay;
		this.display = (XDisplay) managedDisplay.getDisplay();

		this.icccm = new Icccm(this.display);
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
	public void registerClient(final ClientWindow client) {
		final RenderAreaPropertiesManipulator propertiesManipulator = new RenderAreaPropertiesManipulator(
				client);

		this.clientPropertiesManipulators.put(client, propertiesManipulator);
		readProtocolProperties(client);
		installListeners(client);
	}

	@Override
	public boolean requestDelete(final ClientWindow client) {

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
	public boolean offerInput(final ClientWindow client) {
		// TODO we might want to grab the display and ungrab it when the focus
		// is transferred. This way the user is ensured that when he starts
		// typing after an focus transfer, all the input will be send to the
		// correct client.

		final RenderAreaPropertiesManipulator propertiesManipulator = this.clientPropertiesManipulators
				.get(client);
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
				client.giveInputFocus();
			}
			break;
		case NO_INPUT:
			// Don't give any input
			break;
		case PASSIVE_INPUT:
			client.giveInputFocus();
			break;
		}

		return true;
	}

	protected void updateWmState(final ClientWindow client,
			final boolean visible) {
		final WmStateEnum state = visible ? WmStateEnum.NormalState
				: WmStateEnum.WithdrawnState;
		final XWindow iconWindow = this.display.getNoneWindow();
		this.clientPropertiesManipulators.get(client).setPropertyValue(
				IcccmAtoms.WM_STATE_ATOM_NAME,
				new WmStateInstance(this.display, state, iconWindow));
	}

	protected void readProtocolProperties(final ClientWindow client) {
		// TODO update all protocol events for the client so they can be queried
		// in the future
	}

	protected void addClientToSaveSet(final ClientWindow client) {
		final XWindow window = (XWindow) client.getPlatformRenderArea();
		window.addToSaveSet();
	}

	protected void installListeners(final ClientWindow client) {
		client.addEventHandler(new WmHintsListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNormalHintsListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NORMAL_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNameListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NAME_ATOM_NAME));
		client.addEventHandler(new WmClassListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_CLASS_ATOM_NAME));
		client.addEventHandler(new ClientVisibilityListener(),
				GeoEvent.VISIBILITY);
	}

	protected void handleWmHint(final ClientWindow client,
			final WmHintsInstance instance) {

		final long input = instance.getInput();
		final WmStateEnum initialState = instance.getInitialState();
		final XID pixmapid = instance.getIconPixmap();
		final XWindow iconWindow = instance.getIconWindow();
		final int iconX = instance.getIconX();
		final int iconY = instance.getIconY();
		final XID iconMask = instance.getIconMask();
		final XWindow windowGroupLeader = instance.getWindowGroup();

		final int hintFlags = instance.getFlags();
		if ((hintFlags & 1) != 0) {
			// InputHint 1 input

		}
		if ((hintFlags & 2) != 0) {
			// StateHint 2 initial_state

		}
		if ((hintFlags & 4) != 0) {
			// IconPixmapHint 4 icon_pixmap

		}
		if ((hintFlags & 8) != 0) {
			// IconWindowHint 8 icon_window

		}
		if ((hintFlags & 16) != 0) {
			// IconPositionHint 16 icon_x & icon_y

		}
		if ((hintFlags & 32) != 0) {
			// IconMaskHint 32 icon_mask

		}
		if ((hintFlags & 64) != 0) {
			// WindowGroupHint 64 window_group

		}
		if ((hintFlags & 128) != 0) {
			// MessageHint 128 (this bit is obsolete)

		}
		if ((hintFlags & 256) != 0) {
			// UrgencyHint 256 urgency

		}

		// TODO update geometry preferences' visibility
		// TODO update Icon preferences
		// TODO update urgent notify

	}

	protected void handleWmNormalHints(final ClientWindow client,
			final WmSizeHintsInstance propertyInstance) {

		// USPosition 1 User-specified x, y
		// USSize 2 User-specified width, height
		// PPosition 4 Program-specified position
		// PSize 8 Program-specified size
		// PMinSize 16 Program-specified minimum size
		// PMaxSize 32 Program-specified maximum size
		// PResizeInc 64 Program-specified resize increments
		// PAspect 128 Program-specified min and max aspect ratios
		// PBaseSize 256 Program-specified base size
		// PWinGravity 512 Program-specified window gravity

		final int x = propertyInstance.getX();
		final int y = propertyInstance.getY();
		final int width = propertyInstance.getWidth();
		final int height = propertyInstance.getHeight();
		final int minWidth = propertyInstance.getMinWidth();
		final int minHeight = propertyInstance.getMinHeight();
		final int maxWidth = propertyInstance.getMaxWidth();
		final int maxHeight = propertyInstance.getMaxHeight();
		final int widthInc = propertyInstance.getWidthInc();
		final int heightInc = propertyInstance.getHeightInc();

		// parse flags:
		final long normalHintFlags = propertyInstance.getFlags();
		final boolean userSpecifiedPos = (normalHintFlags & 1) != 0;
		final boolean userSpecifiedSize = (normalHintFlags & 2) != 0;
		final boolean programPosition = (normalHintFlags & 4) != 0;
		final boolean programSize = (normalHintFlags & 8) != 0;
		final boolean programMinSize = (normalHintFlags & 16) != 0;
		final boolean programMaxSize = (normalHintFlags & 32) != 0;
		final boolean programResizeIncrements = (normalHintFlags & 64) != 0;
		final boolean programAspectRatios = (normalHintFlags & 128) != 0;
		final boolean programBaseSize = (normalHintFlags & 256) != 0;
		final boolean programResizeGravity = (normalHintFlags & 512) != 0;

		// interpret values:
		if (userSpecifiedPos) {
			// USPosition 1 User-specified x, y
		}
		if (userSpecifiedSize) {
			// USSize 2 User-specified width, height
		}
		if (programPosition) {
			// PPosition 4 Program-specified position

		}
		if (programSize) {
			// PSize 8 Program-specified size
		}
		if (programMinSize) {
			// PMinSize 16 Program-specified minimum size

		}
		if (programMaxSize) {
			// PMaxSize 32 Program-specified maximum size

			// From EWMH:
			// Windows can indicate that they are non-resizable by setting
			// minheight = maxheight and minwidth = maxwidth in the ICCCM
			// WM_NORMAL_HINTS property. The Window Manager MAY decorate such
			// windows differently.

		}
		if (programMinSize && programMaxSize && (minWidth == maxWidth)
				&& (minHeight == maxHeight)) {
			// From EWMH:
			// Windows can indicate that they are non-resizable by setting
			// minheight = maxheight and minwidth = maxwidth in the ICCCM
			// WM_NORMAL_HINTS property. The Window Manager MAY decorate such
			// windows differently.

		}

		if (programResizeIncrements) {
			// PResizeInc 64 Program-specified resize increments

		}
		if (programAspectRatios) {
			// PAspect 128 Program-specified min and max aspect ratios

		}
		if (programBaseSize) {
			// PBaseSize 256 Program-specified base size

		}
		if (programResizeGravity) {
			// PWinGravity 512 Program-specified window gravity

			// Window Managers MUST honor the win_gravity field of
			// WM_NORMAL_HINTS for both MapRequest and ConfigureRequest events
			// (ICCCM Version 2.0, §4.1.2.3 and §4.1.5)

		}

		// TODO update geometry preferences
	}

	protected void handleWmName(final ClientWindow client,
			final PropertyInstanceText propertyInstance) {
		// TODO Update client description

	}

	protected void handleWmClass(final ClientWindow client,
			final PropertyInstanceTexts propertyInstance) {
		final String classInstanceName = propertyInstance.getTexts()[0];
		final String className = propertyInstance.getTexts()[1];
		// TODO Update client description

	}

}
