/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.xcb;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.fusion.x11.core.ByteDataContainer;
import org.fusion.x11.core.DataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.ShortDataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XDisplayPlatform;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.event.XButtonEvent;
import org.fusion.x11.core.event.XClientMessageEvent;
import org.fusion.x11.core.event.XConfigureRequest;
import org.fusion.x11.core.event.XDestroyNotify;
import org.fusion.x11.core.event.XFocusInNotifyEvent;
import org.fusion.x11.core.event.XFocusOutNotifyEvent;
import org.fusion.x11.core.event.XKeyEvent;
import org.fusion.x11.core.event.XMapRequest;
import org.fusion.x11.core.event.XMouseEnterNotify;
import org.fusion.x11.core.event.XMouseLeaveNotify;
import org.fusion.x11.core.event.XPropertyNotify;
import org.fusion.x11.core.event.XSelectionClearNotifyEvent;
import org.fusion.x11.core.event.XSelectionNotifyEvent;
import org.fusion.x11.core.event.XSelectionRequestEvent;
import org.fusion.x11.core.event.XUnknownEvent;
import org.fusion.x11.core.event.XUnmapNotify;
import org.fusion.x11.nativeHelpers.NativeBufferHelper;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.displayinterface.event.ButtonNotifyEvent;
import org.hydrogen.displayinterface.event.DisplayEvent;
import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.input.Button;
import org.hydrogen.displayinterface.input.InputModifiers;
import org.hydrogen.displayinterface.input.Key;
import org.hydrogen.displayinterface.input.KeyboardInput;
import org.hydrogen.displayinterface.input.Momentum;
import org.hydrogen.displayinterface.input.MouseInput;

// TODO documentation
/**
 * An <code>XcbEventParser</code> implements the parsing that is needed to read
 * an <code>XcbEvent</code> from a <code>NativeBufferHelper</code>. It does this
 * by implementing an <code>XcbEventParserHelper</code> for every X event type
 * that should be parsed.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class XcbEventParser {

	private static final Logger LOGGER = Logger.getLogger(XcbEventParser.class);
	private static final String COULD_NOT_CONVERT_EVENT_LOGMESSAGE = "Could not convert X event with code: %d";

	/**
	 * An <code>XcbEventParserHelper</code> implements the parsing logic needed
	 * to read a specific type of Xcb event from a
	 * <code>NativeBufferHelper</code>.
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */

	private abstract class XcbEventParserHelper {

		/**
		 * Parse an <code>XcbEvent</code> from the given
		 * <code>NativeBufferHelper</code> with the given <code>XDisplay</code>
		 * as the source X display of the <code>XcbEvent</code>.
		 * 
		 * @param eventStruct
		 * @param display
		 * @return
		 */
		abstract DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display);

	}

	private final Map<Integer, XcbEventParserHelper> parseMap;

	/**
	 * 
	 * @param displayPlatform
	 */
	public XcbEventParser(final XDisplayPlatform displayPlatform) {
		this.parseMap = new HashMap<Integer, XcbEventParserHelper>();
		bindParserHelpers(displayPlatform);
	}

	/**
	 * General parse method to convert a given window id on on a given
	 * <code>XDisplay</code> to an <code>XWindow</code>. The returned
	 * <code>XWindow</code> will operate as a proxy for the native window,
	 * identified by the given window id, on the given <code>XDisplay</code> .
	 * 
	 * @param display
	 *            An {@link XDisplay}.
	 * @param windowID
	 *            A window id.
	 * @return An {@link XWindow}. @ Thrown when no an invalid window id is
	 *         given or the given <code>XDisplay</code> has an illegal state.
	 */
	private XWindow readXWindow(final XDisplay display, final long windowID) {
		final XID xid = new XID(display, XResourceHandle.valueOf(Long
				.valueOf(windowID)));
		final XWindow returnXWindow = display.getDisplayPlatform()
				.getResourcesRegistry().getClientXWindow(xid);
		return returnXWindow;
	}

	private XAtom readXAtom(final XDisplay display, final long atomId) {
		return display.getDisplayAtoms().getById(Long.valueOf(atomId));
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XMouseLeaveNotify parseXcbMouseLeaveNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t detail;
		// uint16_t sequence;
		// xcb_timestamp_t time; /* Time, in milliseconds the event took place
		// in */
		// xcb_window_t root;
		// xcb_window_t event;
		// xcb_window_t child;
		// int16_t root_x;
		// int16_t root_y;
		// int16_t event_x; /* The x coordinate of the mouse when the event was
		// generated */
		// int16_t event_y; /* The y coordinate of the mouse when the event was
		// generated */
		// uint16_t state; /* A mask of the buttons (or keys) during the event
		// */
		// uint8_t mode; /* The number of mouse button that was clicked */
		// uint8_t same_screen_focus;

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readSignedInt();
		eventStruct.readUnsignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final XMouseLeaveNotify returnXcbMouseLeaveNotify = new XMouseLeaveNotify(
				event);

		return returnXcbMouseLeaveNotify;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XMouseEnterNotify parseXcbMouseEnterNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t detail;
		// uint16_t sequence;
		// xcb_timestamp_t time; /* Time, in milliseconds the event took place
		// in */
		// xcb_window_t root;
		// xcb_window_t event;
		// xcb_window_t child;
		// int16_t root_x;
		// int16_t root_y;
		// int16_t event_x; /* The x coordinate of the mouse when the event was
		// generated */
		// int16_t event_y; /* The y coordinate of the mouse when the event was
		// generated */
		// uint16_t state; /* A mask of the buttons (or keys) during the event
		// */
		// uint8_t mode; /* The number of mouse button that was clicked */
		// uint8_t same_screen_focus;

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readSignedInt();
		eventStruct.readUnsignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final XMouseEnterNotify returnXcbMouseEnterNotify = new XMouseEnterNotify(
				event);

		return returnXcbMouseEnterNotify;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XButtonEvent parseXcbButtonPress(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// xcb_button_t detail
		// uint16_t sequence
		// xcb_timestamp_t time
		// xcb_window_t root
		// xcb_window_t event
		// xcb_window_t child
		// int16_t root_x
		// int16_t root_y
		// int16_t event_x
		// int16_t event_y
		// uint16_t state
		// uint8_t same_screen
		// uint8_t pad0
		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.readSignedInt();
		final int rootX = eventStruct.readSignedShort();
		final int rootY = eventStruct.readSignedShort();
		final int eventX = eventStruct.readSignedShort();
		final int eventY = eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final Button button = new Button(Integer.valueOf(detail));
		final InputModifiers inputModifiers = new InputModifiers(state);
		final MouseInput mouseInput = new MouseInput(Momentum.STARTED, button,
				inputModifiers, rootX, rootY, eventX, eventY);

		final XButtonEvent returnXcbButtonPress = new XButtonEvent(
				ButtonNotifyEvent.PRESSED_TYPE, event, mouseInput);

		return returnXcbButtonPress;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XButtonEvent parseXcbButtonRelease(
			final NativeBufferHelper eventStruct, final XDisplay display)

	{
		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final int rootX = eventStruct.readSignedShort();
		final int rootY = eventStruct.readSignedShort();
		final int eventX = eventStruct.readSignedShort();
		final int eventY = eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final Button button = new Button(Integer.valueOf(detail));
		final InputModifiers inputModifiers = new InputModifiers(state);
		final MouseInput mouseInput = new MouseInput(Momentum.STOPPED, button,
				inputModifiers, rootX, rootY, eventX, eventY);

		final XButtonEvent returnXcbButtonRelease = new XButtonEvent(
				ButtonNotifyEvent.RELEASED_TYPE, event, mouseInput);

		return returnXcbButtonRelease;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XKeyEvent parseXcbKeyPress(final NativeBufferHelper eventStruct,
			final XDisplay display) {

		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final Key key = new Key(detail);
		final InputModifiers inputModifiers = new InputModifiers(state);

		final KeyboardInput input = new KeyboardInput(Momentum.STARTED, key,
				inputModifiers);

		final XKeyEvent xcbKeyPress = new XKeyEvent(KeyNotifyEvent.KEY_PRESSED,
				event, input);

		return xcbKeyPress;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XKeyEvent parseXcbKeyRelease(final NativeBufferHelper eventStruct,
			final XDisplay display) {

		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = readXWindow(display, eventWindowId);

		final Key key = new Key(detail);
		final InputModifiers inputModifiers = new InputModifiers(state);
		final KeyboardInput input = new KeyboardInput(Momentum.STOPPED, key,
				inputModifiers);

		final XKeyEvent xcbKeyRelease = new XKeyEvent(
				KeyNotifyEvent.KEY_RELEASED, event, input);
		return xcbKeyRelease;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XConfigureRequest parseXcbConfigureRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t stack_mode
		// uint16_t sequence
		// xcb_window_t parent
		// xcb_window_t window
		// xcb_window_t sibling
		// int16_t x
		// int16_t y
		// uint16_t width
		// uint16_t height
		// uint16_t border_width
		// uint16_t value_mask
		eventStruct.readUnsignedByte();// uint8_t stack_mode
		eventStruct.readUnsignedShort();// uint16_t sequence
		eventStruct.readUnsignedInt();// xcb_window_t parent
		final long eventWindow = eventStruct.readUnsignedInt();// xcb_window_t
																// window
		eventStruct.readUnsignedInt();// xcb_window_t sibling
		final int x = eventStruct.readSignedShort();// int16_t x
		final int y = eventStruct.readSignedShort();// int16_t y
		final int width = eventStruct.readUnsignedShort();// uint16_t width
		final int height = eventStruct.readUnsignedShort();// uint16_t height
		final int borderWidth = eventStruct.readUnsignedShort();// uint16_t
																// border_width
		final int valueMask = eventStruct.readUnsignedShort();// uint16_t
																// value_mask
		eventStruct.enableWrite();

		final XWindow window = readXWindow(display, eventWindow);

		final XConfigureRequest returnXcbConfigureRequest = new XConfigureRequest(
				window, x, y, width + borderWidth, height + borderWidth,
				valueMask);

		return returnXcbConfigureRequest;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XMapRequest parseXcbMapRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		eventStruct.readUnsignedByte();
		eventStruct.readSignedShort();
		eventStruct.readUnsignedInt();
		final long windowId = eventStruct.readSignedInt();
		eventStruct.enableWrite();

		final XWindow window = readXWindow(display, windowId);

		final XMapRequest returnXcbMapRequest = new XMapRequest(window);
		return returnXcbMapRequest;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XDestroyNotify parseXcbDestroyNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readUnsignedInt();
		final long eventWindow = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow window = readXWindow(display, eventWindow);

		final XDestroyNotify returnXcbDestroyNotify = new XDestroyNotify(window);
		return returnXcbDestroyNotify;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private XPropertyNotify parseXcbPropertyNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t window
		// xcb_atom_t atom
		// xcb_timestamp_t time
		// uint8_t state
		// uint8_t pad1[3]

		// eventStruct
		// .readUnsignedByte();
		// final short pad =
		eventStruct.readUnsignedByte();
		// final int sequence =
		eventStruct.readUnsignedShort();
		final long windowId = eventStruct.readUnsignedInt();
		final long atomId = eventStruct.readUnsignedInt();
		// final long time =
		eventStruct.readUnsignedInt();
		final int state = eventStruct.readUnsignedByte();
		eventStruct.enableWrite();

		final XWindow eventWindow = readXWindow(display, windowId);

		final XAtom atom = readXAtom(display, atomId);

		if (atom instanceof XPropertyXAtom<?>) {
			return new XPropertyNotify(eventWindow,
					(XPropertyXAtom<? extends PropertyInstance>) atom,
					state == XProtocolConstants.PROPERTY_DELETE);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XClientMessageEvent parseXcbClientMessage(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t format
		// uint16_t sequence
		// xcb_window_t window
		// xcb_atom_t type
		// xcb_client_message_data_t data

		final int format = eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long window = eventStruct.readUnsignedInt();
		final long type = eventStruct.readUnsignedInt();
		final byte[] data = new byte[20];
		eventStruct.getBuffer().get(data);

		XClientMessageEvent clientMessage;
		DataContainer<?> dataContainer = null;

		switch (format) {
		case 8: {
			dataContainer = new ByteDataContainer(data);
			break;
		}
		case 16: {
			dataContainer = new ShortDataContainer(data);
			break;
		}
		case 32: {
			dataContainer = new IntDataContainer(data);
			break;
		}
		default:
			break;
		}
		eventStruct.enableWrite();

		final XWindow eventWindow = readXWindow(display, window);

		final XAtom messageAtom = readXAtom(display, type);

		clientMessage = new XClientMessageEvent(eventWindow, messageAtom,
				dataContainer);
		return clientMessage;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private XUnmapNotify parseXcbUnmapNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t event
		// xcb_window_t window
		// uint8_t from_configure
		// eventStruct.readUnsignedByte();
		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();

		final long eventReportedToWindowId = eventStruct.readUnsignedInt();
		final long unmappedWindowId = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		// we are never interested in unmapped childs, we return null which will
		// be seen as an unknown event by the parser and will be ignored.
		if (unmappedWindowId != eventReportedToWindowId) {
			return null;
		}

		final XWindow unmappedWindow = readXWindow(display, unmappedWindowId);

		final XUnmapNotify unmapNotify = new XUnmapNotify(unmappedWindow);

		return unmapNotify;
	}

	private XFocusInNotifyEvent parseXcbFocusInNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long focusInWindowId = eventStruct.readUnsignedInt();
		final XWindow focusInWindow = readXWindow(display, focusInWindowId);
		final XFocusInNotifyEvent focusInNotifyEvent = new XFocusInNotifyEvent(
				focusInWindow);
		return focusInNotifyEvent;
	}

	private XFocusOutNotifyEvent parseXcbFocusOutNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long focusInWindowId = eventStruct.readUnsignedInt();
		final XWindow focusOutWindow = readXWindow(display, focusInWindowId);
		final XFocusOutNotifyEvent focusOutNotifyEvent = new XFocusOutNotifyEvent(
				focusOutWindow);
		return focusOutNotifyEvent;
	}

	private XSelectionRequestEvent parseXcbSelectionRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readUnsignedInt();

		final long ownerWindowId = eventStruct.readUnsignedInt();
		final XWindow owner = readXWindow(display, ownerWindowId);
		final long requestorWindowId = eventStruct.readUnsignedInt();
		final XWindow requestor = readXWindow(display, requestorWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = readXAtom(display, selectionAtomId);
		final long targetAtomId = eventStruct.readUnsignedInt();
		final XAtom targetAtom = readXAtom(display, targetAtomId);
		final long propertyAtomId = eventStruct.readUnsignedInt();
		final XAtom propertyAtom = readXAtom(display, propertyAtomId);

		final XSelectionRequestEvent selectionRequestEvent = new XSelectionRequestEvent(
				owner, requestor, selectionAtom, targetAtom, propertyAtom);

		return selectionRequestEvent;
	}

	private XSelectionNotifyEvent parseXcbSelectionNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readUnsignedInt();
		final long requestorWindowId = eventStruct.readUnsignedInt();
		final XWindow requestor = readXWindow(display, requestorWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = readXAtom(display, selectionAtomId);
		final long targetAtomId = eventStruct.readUnsignedInt();
		final XAtom targetAtom = readXAtom(display, targetAtomId);
		final long propertyAtomId = eventStruct.readUnsignedInt();
		final XAtom propertyAtom = readXAtom(display, propertyAtomId);

		final XSelectionNotifyEvent selectionNotifyEvent = new XSelectionNotifyEvent(
				requestor, selectionAtom, targetAtom, propertyAtom);

		return selectionNotifyEvent;
	}

	private XSelectionClearNotifyEvent parseXcbSelectionClear(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_atom_t selection; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readUnsignedInt();
		final long ownerWindowId = eventStruct.readUnsignedInt();
		final XWindow owner = readXWindow(display, ownerWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = readXAtom(display, selectionAtomId);

		final XSelectionClearNotifyEvent selectionClearNotifyEvent = new XSelectionClearNotifyEvent(
				owner, selectionAtom);

		return selectionClearNotifyEvent;
	}

	private void bindParserHelpers(final XDisplayPlatform xDisplayPlatform) {
		this.parseMap.put(Integer.valueOf(XProtocolConstants.UNMAP_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					XUnmapNotify parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbUnmapNotify(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.BUTTON_PRESS),
				new XcbEventParserHelper() {
					@Override
					XButtonEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbButtonPress(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.BUTTON_RELEASE),
				new XcbEventParserHelper() {
					@Override
					XButtonEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbButtonRelease(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.KEY_PRESS),
				new XcbEventParserHelper() {
					@Override
					XKeyEvent parseEvent(final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbKeyPress(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.KEY_RELEASE),
				new XcbEventParserHelper() {
					@Override
					XKeyEvent parseEvent(final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbKeyRelease(eventStruct, display);
					}
				});

		this.parseMap.put(
				Integer.valueOf(XProtocolConstants.CONFIGURE_REQUEST),
				new XcbEventParserHelper() {
					@Override
					XConfigureRequest parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbConfigureRequest(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.MAP_REQUEST),
				new XcbEventParserHelper() {
					@Override
					XMapRequest parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbMapRequest(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.DESTROY_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					XDestroyNotify parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbDestroyNotify(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.PROPERTY_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					XPropertyNotify parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbPropertyNotify(eventStruct, display);
					}
				});

		this.parseMap.put(Integer.valueOf(XProtocolConstants.CLIENT_MESSAGE),
				new XcbEventParserHelper() {
					@Override
					XClientMessageEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbClientMessage(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.ENTER_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbMouseEnterNotify(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.LEAVE_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbMouseLeaveNotify(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.FOCUS_IN),
				new XcbEventParserHelper() {

					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbFocusInNotify(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.FOCUS_OUT),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbFocusOutNotify(eventStruct, display);
					}
				});
		this.parseMap.put(
				Integer.valueOf(XProtocolConstants.SELECTION_REQUEST),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbSelectionRequest(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.SELECTION_NOTIFY),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbSelectionNotify(eventStruct, display);
					}
				});
		this.parseMap.put(Integer.valueOf(XProtocolConstants.SELECTION_CLEAR),
				new XcbEventParserHelper() {
					@Override
					DisplayEvent parseEvent(
							final NativeBufferHelper eventStruct,
							final XDisplay display) {
						return parseXcbSelectionClear(eventStruct, display);
					}
				});
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
			final XDisplay display) {

		final int eventCode = readEventCode(eventStruct);

		final XcbEventParserHelper xcbEventParserHelper = this.parseMap
				.get(Integer.valueOf(eventCode));

		DisplayEvent returnXcbEvent;

		if (xcbEventParserHelper == null) {
			XcbEventParser.LOGGER.warn(String.format(
					XcbEventParser.COULD_NOT_CONVERT_EVENT_LOGMESSAGE,
					eventCode));

			returnXcbEvent = new XUnknownEvent();
			eventStruct.enableWrite();
		} else {
			returnXcbEvent = xcbEventParserHelper.parseEvent(eventStruct,
					display);
		}

		if (returnXcbEvent == null) {
			returnXcbEvent = new XUnknownEvent();
		}

		return returnXcbEvent;
	}

	/**
	 * Read the event type code from the given <code>NativeBufferHelper</code>.
	 * The returned event type code identifies the event that is stored in the
	 * given <code>NativeBufferHelper</code>.
	 * 
	 * @param nativeBufferHelper
	 * @return an event type code.
	 */
	private short readEventCode(final NativeBufferHelper nativeBufferHelper) {
		final long eventCodeByte = nativeBufferHelper.readUnsignedByte();
		final short eventCode = (short) (eventCodeByte & ~0x80);
		return eventCode;
	}
}
