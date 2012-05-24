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
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.event.XButtonEvent;
import org.fusion.x11.core.event.XClientMessageEvent;
import org.fusion.x11.core.event.XConfigureNotify;
import org.fusion.x11.core.event.XConfigureRequest;
import org.fusion.x11.core.event.XDestroyNotify;
import org.fusion.x11.core.event.XFocusInNotifyEvent;
import org.fusion.x11.core.event.XFocusOutNotifyEvent;
import org.fusion.x11.core.event.XKeyEvent;
import org.fusion.x11.core.event.XMapNotify;
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
import org.trinity.core.display.api.PropertyInstance;
import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.input.api.Momentum;
import org.trinity.core.input.impl.BaseButton;
import org.trinity.core.input.impl.BaseInputModifiers;
import org.trinity.core.input.impl.BaseKey;
import org.trinity.core.input.impl.BaseKeyboardInput;
import org.trinity.core.input.impl.BaseMouseInput;

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

	private static abstract class XcbEventParserHelper {

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

	private static final class UnmapNotifyParser extends XcbEventParserHelper {
		@Override
		XUnmapNotify parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbUnmapNotify(eventStruct, display);
		}
	}

	private static final UnmapNotifyParser UNMAP_NOTIFY_PARSER = new UnmapNotifyParser();

	private static final class ButtonPressNotifyParser extends
			XcbEventParserHelper {
		@Override
		XButtonEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbButtonPress(eventStruct, display);
		}
	}

	private static final ButtonPressNotifyParser BUTTON_PRESS_NOTIFY_PARSER = new ButtonPressNotifyParser();

	private static final class ButtonReleaseNotifyParser extends
			XcbEventParserHelper {
		@Override
		XButtonEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbButtonRelease(eventStruct, display);
		}
	}

	private static final ButtonReleaseNotifyParser BUTTON_RELEASE_NOTIFY_PARSER = new ButtonReleaseNotifyParser();

	private static final class KeyPressNotifyParser extends
			XcbEventParserHelper {
		@Override
		XKeyEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbKeyPress(eventStruct, display);
		}
	}

	private static final KeyPressNotifyParser KEY_PRESS_NOTIFY_PARSER = new KeyPressNotifyParser();

	private static final class KeyReleaseNotifyParser extends
			XcbEventParserHelper {
		@Override
		XKeyEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbKeyRelease(eventStruct, display);
		}
	}

	private static final KeyReleaseNotifyParser KEY_RELEASE_NOTIFY_PARSER = new KeyReleaseNotifyParser();

	private static final class ConfigureRequestParser extends
			XcbEventParserHelper {
		@Override
		XConfigureRequest parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser
					.parseXcbConfigureRequest(eventStruct, display);
		}
	}

	private static final ConfigureRequestParser CONFIGURE_REQUEST_PARSER = new ConfigureRequestParser();

	private static final class MapRequestParser extends XcbEventParserHelper {
		@Override
		XMapRequest parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbMapRequest(eventStruct, display);
		}
	}

	private static final MapRequestParser MAP_REQUEST_PARSER = new MapRequestParser();

	private static final class DestroyNotifyParser extends XcbEventParserHelper {
		@Override
		XDestroyNotify parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbDestroyNotify(eventStruct, display);
		}
	}

	private static final DestroyNotifyParser DESTROY_NOTIFY_PARSER = new DestroyNotifyParser();

	private static final class PropertyNotifyParser extends
			XcbEventParserHelper {
		@Override
		XPropertyNotify parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbPropertyNotify(eventStruct, display);
		}
	}

	private static final PropertyNotifyParser PROPERTY_NOTIFY_PARSER = new PropertyNotifyParser();

	private static final class ClientMessageParser extends XcbEventParserHelper {
		@Override
		XClientMessageEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbClientMessage(eventStruct, display);
		}
	}

	private static final ClientMessageParser CLIENT_MESSAGE_PARSER = new ClientMessageParser();

	private static final class MouseEnterNotifyParser extends
			XcbEventParserHelper {
		@Override
		DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser
					.parseXcbMouseEnterNotify(eventStruct, display);
		}
	}

	private static final MouseEnterNotifyParser MOUSE_ENTER_NOTIFY_PARSER = new MouseEnterNotifyParser();

	private static final class MouseLeaveNotifyParser extends
			XcbEventParserHelper {
		@Override
		DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser
					.parseXcbMouseLeaveNotify(eventStruct, display);
		}
	}

	private static final MouseLeaveNotifyParser MOUSE_LEAVE_NOTIFY_PARSER = new MouseLeaveNotifyParser();

	private static final class FocusInNotifyParser extends XcbEventParserHelper {
		@Override
		DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbFocusInNotify(eventStruct, display);
		}
	}

	private static final FocusInNotifyParser FOCUS_IN_NOTIFY_PARSER = new FocusInNotifyParser();

	private static final class FocusOutNotifyParser extends
			XcbEventParserHelper {
		@Override
		DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbFocusOutNotify(eventStruct, display);
		}
	}

	private static final FocusOutNotifyParser FOCUS_OUT_NOTIFY_PARSER = new FocusOutNotifyParser();

	private static final class SelectionRequestParser extends
			XcbEventParserHelper {
		@Override
		XSelectionRequestEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser
					.parseXcbSelectionRequest(eventStruct, display);
		}
	}

	private static final SelectionRequestParser SELECTION_REQUEST_PARSER = new SelectionRequestParser();

	private static final class SelectionNotifyParser extends
			XcbEventParserHelper {
		@Override
		XSelectionNotifyEvent parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbSelectionNotify(eventStruct, display);
		}
	}

	private static final SelectionNotifyParser SELECTION_NOTIFY_PARSER = new SelectionNotifyParser();

	private static final class SelectionClearParser extends
			XcbEventParserHelper {
		@Override
		XSelectionClearNotifyEvent parseEvent(
				final NativeBufferHelper eventStruct, final XDisplay display) {
			return XcbEventParser.parseXcbSelectionClear(eventStruct, display);
		}
	}

	private static final SelectionClearParser SELECTION_CLEAR_PARSER = new SelectionClearParser();

	private static final class ConfigureNotifyParser extends
			XcbEventParserHelper {
		@Override
		XConfigureNotify parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbConfigureNotify(eventStruct, display);
		}
	}

	private static final ConfigureNotifyParser CONFIGURE_NOTIFY_PARSER = new ConfigureNotifyParser();

	private static final class MapNotifyParser extends XcbEventParserHelper {
		@Override
		XMapNotify parseEvent(final NativeBufferHelper eventStruct,
				final XDisplay display) {
			return XcbEventParser.parseXcbMapNotify(eventStruct, display);
		}
	}

	private static final MapNotifyParser MAP_NOTIFY_PARSER = new MapNotifyParser();

	private XcbEventParser() {
	}

	private static final Map<Integer, XcbEventParserHelper> PARSERS_MAP = new HashMap<Integer, XcbEventParser.XcbEventParserHelper>();

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
	private static XWindow readXWindow(final XDisplay display,
			final long windowID) {
		final XID xid = new XID(display, XResourceHandle.valueOf(Long
				.valueOf(windowID)));
		final XWindow returnXWindow = display.getDisplayPlatform()
				.getResourcesRegistry().getClientXWindow(xid);
		return returnXWindow;
	}

	private static XAtom readXAtom(final XDisplay display, final long atomId) {
		return display.getDisplayAtoms().getById(Long.valueOf(atomId));
	}

	private static XConfigureNotify parseXcbConfigureNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer;
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		// xcb_window_t above_sibling; /**< */
		// int16_t x; /**< */
		// int16_t y; /**< */
		// uint16_t width; /**< */
		// uint16_t height; /**< */
		// uint16_t border_width; /**< */
		// uint8_t override_redirect; /**< */
		// uint8_t pad1; /**< */

		eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readUnsignedInt();
		final long windowId = eventStruct.readUnsignedInt();
		final XWindow window = XcbEventParser.readXWindow(display, windowId);
		eventStruct.readUnsignedInt();
		final int x = eventStruct.readUnsignedShort();
		final int y = eventStruct.readUnsignedShort();
		final int width = (int) eventStruct.readUnsignedInt();
		final int height = (int) eventStruct.readUnsignedInt();

		final XConfigureNotify configureNotify = new XConfigureNotify(window,
				x, y, width, height);
		return configureNotify;
	}

	private static XMapNotify parseXcbMapNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		// uint8_t override_redirect; /**< */
		// uint8_t pad1[3]; /**< */

		eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		eventStruct.readUnsignedInt();
		final long windowId = eventStruct.readUnsignedInt();
		final XWindow window = XcbEventParser.readXWindow(display, windowId);
		eventStruct.readUnsignedInt();

		final XMapNotify mapNotify = new XMapNotify(window);
		return mapNotify;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XMouseLeaveNotify parseXcbMouseLeaveNotify(
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
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readUnsignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

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
	private static XMouseEnterNotify parseXcbMouseEnterNotify(
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
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readUnsignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

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
	private static XButtonEvent parseXcbButtonPress(
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
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readUnsignedInt();
		eventStruct.readSignedInt();
		final int rootX = eventStruct.readSignedShort();
		final int rootY = eventStruct.readSignedShort();
		final int eventX = eventStruct.readSignedShort();
		final int eventY = eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

		final BaseButton baseButton = new BaseButton(Integer.valueOf(detail));
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);
		final BaseMouseInput baseMouseInput = new BaseMouseInput(
				Momentum.STARTED, baseButton, baseInputModifiers, rootX, rootY,
				eventX, eventY);

		final XButtonEvent returnXcbButtonPress = new XButtonEvent(
				DisplayEventType.BUTTON_PRESSED, event, baseMouseInput);

		return returnXcbButtonPress;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XButtonEvent parseXcbButtonRelease(
			final NativeBufferHelper eventStruct, final XDisplay display)

	{
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
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		final int rootX = eventStruct.readSignedShort();
		final int rootY = eventStruct.readSignedShort();
		final int eventX = eventStruct.readSignedShort();
		final int eventY = eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

		final BaseButton baseButton = new BaseButton(Integer.valueOf(detail));
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);
		final BaseMouseInput baseMouseInput = new BaseMouseInput(
				Momentum.STOPPED, baseButton, baseInputModifiers, rootX, rootY,
				eventX, eventY);

		final XButtonEvent returnXcbButtonRelease = new XButtonEvent(
				DisplayEventType.BUTTON_RELEASED, event, baseMouseInput);

		return returnXcbButtonRelease;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XKeyEvent parseXcbKeyPress(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// xcb_keycode_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t root; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t child; /**< */
		// int16_t root_x; /**< */
		// int16_t root_y; /**< */
		// int16_t event_x; /**< */
		// int16_t event_y; /**< */
		// uint16_t state; /**< */
		// uint8_t same_screen; /**< */
		// uint8_t pad0; /**< */
		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

		final BaseKey baseKey = new BaseKey(detail);
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);

		final BaseKeyboardInput input = new BaseKeyboardInput(Momentum.STARTED,
				baseKey, baseInputModifiers);

		final XKeyEvent xcbKeyPress = new XKeyEvent(
				DisplayEventType.KEY_PRESSED, event, input);

		return xcbKeyPress;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XKeyEvent parseXcbKeyRelease(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// xcb_keycode_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t root; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t child; /**< */
		// int16_t root_x; /**< */
		// int16_t root_y; /**< */
		// int16_t event_x; /**< */
		// int16_t event_y; /**< */
		// uint16_t state; /**< */
		// uint8_t same_screen; /**< */
		// uint8_t pad0; /**< */
		final int detail = eventStruct.readSignedByte();
		eventStruct.readSignedShort();
		final int timestamp = eventStruct.readSignedInt();
		display.setLastServerTime(timestamp);
		eventStruct.readSignedInt();
		final long eventWindowId = eventStruct.readSignedInt();
		eventStruct.readSignedInt();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		eventStruct.readSignedShort();
		final int state = eventStruct.readUnsignedShort();
		eventStruct.enableWrite();

		final XWindow event = XcbEventParser
				.readXWindow(display, eventWindowId);

		final BaseKey baseKey = new BaseKey(detail);
		final BaseInputModifiers baseInputModifiers = new BaseInputModifiers(
				state);
		final BaseKeyboardInput input = new BaseKeyboardInput(Momentum.STOPPED,
				baseKey, baseInputModifiers);

		final XKeyEvent xcbKeyRelease = new XKeyEvent(
				DisplayEventType.KEY_RELEASED, event, input);
		return xcbKeyRelease;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XConfigureRequest parseXcbConfigureRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t stack_mode; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t parent; /**< */
		// xcb_window_t window; /**< */
		// xcb_window_t sibling; /**< */
		// int16_t x; /**< */
		// int16_t y; /**< */
		// uint16_t width; /**< */
		// uint16_t height; /**< */
		// uint16_t border_width; /**< */
		// uint16_t value_mask; /**< */
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

		final XWindow window = XcbEventParser.readXWindow(display, eventWindow);

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
	private static XMapRequest parseXcbMapRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t parent; /**< */
		// xcb_window_t window; /**< */
		eventStruct.readUnsignedByte();
		eventStruct.readSignedShort();
		eventStruct.readUnsignedInt();
		final long windowId = eventStruct.readSignedInt();
		eventStruct.enableWrite();

		final XWindow window = XcbEventParser.readXWindow(display, windowId);

		final XMapRequest returnXcbMapRequest = new XMapRequest(window);
		return returnXcbMapRequest;
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	private static XDestroyNotify parseXcbDestroyNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// xcb_window_t window; /**< */
		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		eventStruct.readUnsignedInt();
		final long eventWindow = eventStruct.readUnsignedInt();
		eventStruct.enableWrite();

		final XWindow window = XcbEventParser.readXWindow(display, eventWindow);

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
	private static XPropertyNotify parseXcbPropertyNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t window
		// xcb_atom_t atom
		// xcb_timestamp_t time
		// uint8_t state
		// uint8_t pad1[3]

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long windowId = eventStruct.readUnsignedInt();
		final long atomId = eventStruct.readUnsignedInt();
		final int time = eventStruct.readSignedInt();
		display.setLastServerTime(time);
		final int state = eventStruct.readUnsignedByte();
		eventStruct.enableWrite();

		final XWindow eventWindow = XcbEventParser.readXWindow(display,
				windowId);

		final XAtom atom = XcbEventParser.readXAtom(display, atomId);

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
	private static XClientMessageEvent parseXcbClientMessage(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t response_type; /**< */
		// uint8_t format; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t window; /**< */
		// xcb_atom_t type; /**< */
		// xcb_client_message_data_t data; /**< */

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

		final XWindow eventWindow = XcbEventParser.readXWindow(display, window);

		final XAtom messageAtom = XcbEventParser.readXAtom(display, type);

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
	private static XUnmapNotify parseXcbUnmapNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// Contents of native buffer:
		// uint8_t pad0
		// uint16_t sequence
		// xcb_window_t event
		// xcb_window_t window
		// uint8_t from_configure

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

		final XWindow unmappedWindow = XcbEventParser.readXWindow(display,
				unmappedWindowId);

		final XUnmapNotify unmapNotify = new XUnmapNotify(unmappedWindow);

		return unmapNotify;
	}

	private static XFocusInNotifyEvent parseXcbFocusInNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long focusInWindowId = eventStruct.readUnsignedInt();
		final XWindow focusInWindow = XcbEventParser.readXWindow(display,
				focusInWindowId);
		final XFocusInNotifyEvent focusInNotifyEvent = new XFocusInNotifyEvent(
				focusInWindow);
		return focusInNotifyEvent;
	}

	private static XFocusOutNotifyEvent parseXcbFocusOutNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t detail; /**< */
		// uint16_t sequence; /**< */
		// xcb_window_t event; /**< */
		// uint8_t mode; /**< */
		// uint8_t pad0[3]; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final long focusInWindowId = eventStruct.readUnsignedInt();
		final XWindow focusOutWindow = XcbEventParser.readXWindow(display,
				focusInWindowId);
		final XFocusOutNotifyEvent focusOutNotifyEvent = new XFocusOutNotifyEvent(
				focusOutWindow);
		return focusOutNotifyEvent;
	}

	private static XSelectionRequestEvent parseXcbSelectionRequest(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
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
		final int time = eventStruct.readSignedInt();
		display.setLastServerTime(time);

		final long ownerWindowId = eventStruct.readUnsignedInt();
		final XWindow owner = XcbEventParser
				.readXWindow(display, ownerWindowId);
		final long requestorWindowId = eventStruct.readUnsignedInt();
		final XWindow requestor = XcbEventParser.readXWindow(display,
				requestorWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = XcbEventParser.readXAtom(display,
				selectionAtomId);
		final long targetAtomId = eventStruct.readUnsignedInt();
		final XAtom targetAtom = XcbEventParser
				.readXAtom(display, targetAtomId);
		final long propertyAtomId = eventStruct.readUnsignedInt();
		final XAtom propertyAtom = XcbEventParser.readXAtom(display,
				propertyAtomId);

		final XSelectionRequestEvent selectionRequestEvent = new XSelectionRequestEvent(
				owner, requestor, selectionAtom, targetAtom, propertyAtom);

		return selectionRequestEvent;
	}

	private static XSelectionNotifyEvent parseXcbSelectionNotify(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t requestor; /**< */
		// xcb_atom_t selection; /**< */
		// xcb_atom_t target; /**< */
		// xcb_atom_t property; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final int time = eventStruct.readSignedInt();
		display.setLastServerTime(time);
		final long requestorWindowId = eventStruct.readUnsignedInt();
		final XWindow requestor = XcbEventParser.readXWindow(display,
				requestorWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = XcbEventParser.readXAtom(display,
				selectionAtomId);
		final long targetAtomId = eventStruct.readUnsignedInt();
		final XAtom targetAtom = XcbEventParser
				.readXAtom(display, targetAtomId);
		final long propertyAtomId = eventStruct.readUnsignedInt();
		final XAtom propertyAtom = XcbEventParser.readXAtom(display,
				propertyAtomId);

		final XSelectionNotifyEvent selectionNotifyEvent = new XSelectionNotifyEvent(
				requestor, selectionAtom, targetAtom, propertyAtom);

		return selectionNotifyEvent;
	}

	private static XSelectionClearNotifyEvent parseXcbSelectionClear(
			final NativeBufferHelper eventStruct, final XDisplay display) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_atom_t selection; /**< */

		eventStruct.readUnsignedByte();
		eventStruct.readUnsignedShort();
		final int time = eventStruct.readSignedInt();
		display.setLastServerTime(time);
		final long ownerWindowId = eventStruct.readUnsignedInt();
		final XWindow owner = XcbEventParser
				.readXWindow(display, ownerWindowId);
		final long selectionAtomId = eventStruct.readUnsignedInt();
		final XAtom selectionAtom = XcbEventParser.readXAtom(display,
				selectionAtomId);

		final XSelectionClearNotifyEvent selectionClearNotifyEvent = new XSelectionClearNotifyEvent(
				owner, selectionAtom);

		return selectionClearNotifyEvent;
	}

	static {
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.UNMAP_NOTIFY),
				XcbEventParser.UNMAP_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.BUTTON_PRESS),
				XcbEventParser.BUTTON_PRESS_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.BUTTON_RELEASE),
				XcbEventParser.BUTTON_RELEASE_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.KEY_PRESS),
				XcbEventParser.KEY_PRESS_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.KEY_RELEASE),
				XcbEventParser.KEY_RELEASE_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.CONFIGURE_REQUEST),
				XcbEventParser.CONFIGURE_REQUEST_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.MAP_REQUEST),
				XcbEventParser.MAP_REQUEST_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.DESTROY_NOTIFY),
				XcbEventParser.DESTROY_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.PROPERTY_NOTIFY),
				XcbEventParser.PROPERTY_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.CLIENT_MESSAGE),
				XcbEventParser.CLIENT_MESSAGE_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.ENTER_NOTIFY),
				XcbEventParser.MOUSE_ENTER_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.LEAVE_NOTIFY),
				XcbEventParser.MOUSE_LEAVE_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.FOCUS_IN),
				XcbEventParser.FOCUS_IN_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.FOCUS_OUT),
				XcbEventParser.FOCUS_OUT_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.SELECTION_REQUEST),
				XcbEventParser.SELECTION_REQUEST_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.SELECTION_NOTIFY),
				XcbEventParser.SELECTION_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.SELECTION_CLEAR),
				XcbEventParser.SELECTION_CLEAR_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.MAP_NOTIFY),
				XcbEventParser.MAP_NOTIFY_PARSER);
		XcbEventParser.PARSERS_MAP.put(
				Integer.valueOf(XProtocolConstants.CONFIGURE_NOTIFY),
				XcbEventParser.CONFIGURE_NOTIFY_PARSER);
	}

	/**
	 * 
	 * @param eventStruct
	 * @param display
	 * @return
	 */
	static DisplayEvent parseEvent(final NativeBufferHelper eventStruct,
			final XDisplay display) {

		final int eventCode = XcbEventParser.readEventCode(eventStruct);

		final XcbEventParserHelper xcbEventParserHelper = XcbEventParser.PARSERS_MAP
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
	private static short readEventCode(
			final NativeBufferHelper nativeBufferHelper) {
		final long eventCodeByte = nativeBufferHelper.readUnsignedByte();
		final short eventCode = (short) (eventCodeByte & ~0x80);
		return eventCode;
	}
}
