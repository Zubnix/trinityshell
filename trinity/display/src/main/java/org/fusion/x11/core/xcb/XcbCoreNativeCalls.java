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

import java.nio.ByteBuffer;

import org.fusion.x11.core.XCoreNative;
import org.fusion.x11.core.XWindowAttributes;
import org.fusion.x11.core.XWindowGeometry;
import org.fusion.x11.nativeHelpers.NativeBufferHelper;
import org.fusion.x11.nativeHelpers.XNativeCall;
import org.trinity.core.geometry.api.Coordinates;
import org.trinity.core.geometry.impl.BaseCoordinates;

/**
 * An <code>XcbCoreNativeCalls</code> statically groups all native calls that
 * are needed by the <code>XcbCoreInterfaceImpl</code> to talk to the X display
 * server. Every native call is wrapped in an {@link XNativeCall} for return
 * value parsing and error handling.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XcbCoreNativeCalls {

	public XNativeCall<Coordinates, Long, Number> getCallTranslateCoordinates() {
		return this.translateCoordinates;
	}

	private final TranslateCoordinates translateCoordinates;

	public static final class TranslateCoordinates extends
			XNativeCall<Coordinates, Long, Number> {
		private TranslateCoordinates() {
		}

		@Override
		public Coordinates getResult() {
			final int destX = getNativeBufferHelper().readUnsignedShort();
			final int destY = getNativeBufferHelper().readUnsignedShort();
			final Coordinates xCoordinates = new BaseCoordinates(destX, destY);
			return xCoordinates;
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeTranslateCoordinates(getDisplayPeer()
					.longValue(), getArgs()[0].longValue(), getArgs()[1]
					.longValue(), getArgs()[2].intValue(), getArgs()[3]
					.intValue(), getNativeBufferHelper().getBuffer());
		}
	}

	public RemoveFromSaveSet getCallRemoveFromSaveSet() {
		return this.removeFromSaveSet;
	}

	private final RemoveFromSaveSet removeFromSaveSet;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class RemoveFromSaveSet extends
			XNativeCall<Void, Long, Long> {
		private RemoveFromSaveSet() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeRemoveFromSaveSet(getDisplayPeer()
					.longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	public AddToSaveSet getCallAddToSaveSet() {
		return this.addToSaveSet;
	}

	private final AddToSaveSet addToSaveSet;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class AddToSaveSet extends
			XNativeCall<Void, Long, Long> {
		private AddToSaveSet() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeAddToSaveSet(getDisplayPeer().longValue(),
					getArgs()[0].longValue(), getNativeBufferHelper()
							.getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public SetSelectionOwner getCallSetSelectionOwner() {
		return this.setSelectionOwner;
	}

	private final SetSelectionOwner setSelectionOwner;

	/**
	 * args: (Long) atom id, (Long) window id, (Integer) time
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class SetSelectionOwner extends
			XNativeCall<Void, Long, Number> {
		private SetSelectionOwner() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeSetSelectionOwner(getDisplayPeer()
					.longValue(), getArgs()[0].longValue(), getArgs()[1]
					.longValue(), getArgs()[2].intValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	public GetSelectionOwner getCallSelectionOwner() {
		return this.getSelectionOwner;
	}

	private final GetSelectionOwner getSelectionOwner;

	/**
	 * args: (Long) atom id
	 * <p>
	 * return: (Long) window id
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static final class GetSelectionOwner extends
			XNativeCall<Long, Long, Long> {
		private GetSelectionOwner() {
		}

		@Override
		public Long getResult() {
			final long windowId = getNativeBufferHelper().readUnsignedInt();
			return Long.valueOf(windowId);
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeGetSelectionOwner(getDisplayPeer()
					.longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	public CreateNewWindow getCallCreateNewWindow() {
		return this.createNewWindow;
	}

	private final CreateNewWindow createNewWindow;

	/**
	 * args: (Void) None
	 * <p>
	 * return: (Long) window id
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static final class CreateNewWindow extends
			XNativeCall<Long, Long, Void> {
		private CreateNewWindow() {
		}

		@Override
		public Long getResult() {
			final long windowId = getNativeBufferHelper().readUnsignedInt();
			return Long.valueOf(windowId);
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeCreateNewWindow(getDisplayPeer()
					.longValue(), getNativeBufferHelper().getBuffer());
		}
	}

	public DestroyWindow getCallDestroyWindow() {
		return this.destroyWindow;
	}

	private final DestroyWindow destroyWindow;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class DestroyWindow extends
			XNativeCall<Void, Long, Long> {
		private DestroyWindow() {
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeDestroy(
					getDisplayPeer(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	public EnableEvents getCallEnableEvents() {
		return this.enableEvents;
	}

	private final EnableEvents enableEvents;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class EnableEvents extends
			XNativeCall<Void, Long, Long> {
		private EnableEvents() {
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativePropagateEvent(
					getDisplayPeer(), getArgs()[0].longValue(), getArgs()[1]
							.longValue(), getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Flush getCallFlush() {
		return this.flush;
	}

	private final Flush flush;

	/**
	 * args: (Void) NONE
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class Flush extends XNativeCall<Void, Long, Void> {
		private Flush() {
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeFlush(
					getDisplayPeer(), getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetChildWindows getCallGetChildWindows() {
		return this.getChildWindows;
	}

	private final GetChildWindows getChildWindows;

	/**
	 * args: (Long) parent window id
	 * <p>
	 * return: (Long[]) child window ids
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetChildWindows extends
			XNativeCall<Long[], Long, Long> {
		private GetChildWindows() {
		}

		@Override
		public Long[] getResult() {
			final long nroChildren = getNativeBufferHelper().readUnsignedInt();
			final Long[] children = new Long[(int) nroChildren];
			for (int i = 0; i < children.length; i++) {
				final long childId = getNativeBufferHelper().readUnsignedInt();
				children[i] = Long.valueOf(childId);
			}
			getNativeBufferHelper().enableWrite();

			return children;
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeGetChildren(
					getDisplayPeer(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetNextEvent getCallGetNextEvent() {
		return this.getNextEvent;
	}

	private final GetNextEvent getNextEvent;

	/**
	 * args: (Void) NONE
	 * <p>
	 * return: (NativeBufferHelper) event contents
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetNextEvent extends
			XNativeCall<NativeBufferHelper, Long, Void> {
		private GetNextEvent() {
		}

		@Override
		public NativeBufferHelper getResult() {
			final NativeBufferHelper returnNativeBufferHelper = getNativeBufferHelper();
			return returnNativeBufferHelper;
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeGetNextEvent(
					getDisplayPeer(), getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetRootWindow getCallGetRootWindow() {
		return this.getRootWindow;
	}

	private final GetRootWindow getRootWindow;

	/**
	 * args: (Void) NONE
	 * <p>
	 * return: (Long) root window id
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetRootWindow extends
			XNativeCall<Long, Long, Void> {
		private GetRootWindow() {
		}

		@Override
		public Long getResult() {
			final Long returnLong = Long.valueOf(getNativeBufferHelper()
					.readUnsignedInt());
			getNativeBufferHelper().enableWrite();
			return returnLong;
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeGetRootWindow(
					getDisplayPeer().longValue(), getNativeBufferHelper()
							.getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetWindowAttributesCopy getCallGetWindowAttributesCopy() {
		return this.getWindowAttributesCopy;
	}

	private final GetWindowAttributesCopy getWindowAttributesCopy;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (XWindowAttributes) window attributes
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetWindowAttributesCopy extends
			XNativeCall<XWindowAttributes, Long, Long> {
		private GetWindowAttributesCopy() {
		}

		@Override
		public XWindowAttributes getResult() {
			// uint8_t response_type; /**< */
			// uint8_t backing_store; /**< */
			// uint16_t sequence; /**< */
			// uint32_t length; /**< */
			// xcb_visualid_t visual; /**< */
			// uint16_t _class; /**< */
			// uint8_t bit_gravity; /**< */
			// uint8_t win_gravity; /**< */
			// uint32_t backing_planes; /**< */
			// uint32_t backing_pixel; /**< */
			// uint8_t save_under; /**< */
			// uint8_t map_is_installed; /**< */
			// uint8_t map_state; /**< */
			// uint8_t override_redirect; /**< */
			// xcb_colormap_t colormap; /**< */
			// uint32_t all_event_masks; /**< */
			// uint32_t your_event_mask; /**< */
			// uint16_t do_not_propagate_mask; /**< */
			// uint8_t pad0[2]; /**< */

			getNativeBufferHelper().readUnsignedByte(); // uint8_t
			// response_type
			getNativeBufferHelper().readUnsignedByte();// uint8_t
			// backing_store
			getNativeBufferHelper().readUnsignedShort();// uint16_t
			// sequence
			getNativeBufferHelper().readUnsignedInt();// uint32_t
			// length
			final long visualPeer = getNativeBufferHelper().readUnsignedInt();// xcb_visualid_t
			// visual
			getNativeBufferHelper().readUnsignedShort();// uint16_t
			// _class
			getNativeBufferHelper().readUnsignedByte();// uint8_t
			// bit_gravity
			getNativeBufferHelper().readUnsignedByte();// uint8_t
			// win_gravity
			getNativeBufferHelper().readUnsignedInt();// uint32_t
			// backing_planes
			getNativeBufferHelper().readUnsignedInt();// uint32_t
			// backing_pixel
			getNativeBufferHelper().readUnsignedByte();// uint8_t
			// save_under
			getNativeBufferHelper().readUnsignedByte();// uint8_t
			// map_is_installed
			final int mapState = getNativeBufferHelper().readUnsignedByte();// uint8_t
			// map_state
			final boolean overrideRedirect = getNativeBufferHelper()
					.readBoolean();// uint8_t
			// override_redirect
			getNativeBufferHelper().readUnsignedInt();// xcb_colormap_t
			// colormap
			getNativeBufferHelper().readUnsignedInt();// uint32_t
			// all_event_masks
			getNativeBufferHelper().readUnsignedInt();// uint32_t
			// your_event_mask
			getNativeBufferHelper().readUnsignedShort();// uint16_t
			// do_not_propagate_mask
			// pad
			getNativeBufferHelper().enableWrite();
			final XWindowAttributes wa = new XWindowAttributes(mapState,
					overrideRedirect, visualPeer);

			return wa;
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative
					.nativeGetWindowAttributes(getDisplayPeer().longValue(),
							getArgs()[0].longValue(), getNativeBufferHelper()
									.getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetWindowGeometry getCallGetWindowGeometry() {
		return this.getWindowGeometry;
	}

	private final GetWindowGeometry getWindowGeometry;

	/**
	 * 
	 * args: (Long) window id
	 * <p>
	 * return: (XWindowGeometry) window geometry
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetWindowGeometry extends
			XNativeCall<XWindowGeometry, Long, Long> {
		private GetWindowGeometry() {
		}

		@Override
		public XWindowGeometry getResult() {

			// Contents of native buffer:
			// uint8_t response_type; /**< */
			// uint8_t depth; /**< */
			// uint16_t sequence; /**< */
			// uint32_t length; /**< */
			// xcb_window_t root; /**< */
			// int16_t x; /**< */
			// int16_t y; /**< */
			// uint16_t width; /**< */
			// uint16_t height; /**< */
			// uint16_t border_width; /**< */
			// uint8_t pad0[2]; /**< */

			final NativeBufferHelper nativeBufferHelper = getNativeBufferHelper();
			// final short responseType =
			nativeBufferHelper.readUnsignedByte();
			// final short depth =
			nativeBufferHelper.readUnsignedByte();
			// final int sequence =
			nativeBufferHelper.readUnsignedShort();
			// final long lenght =
			nativeBufferHelper.readUnsignedInt();
			// final long root =
			nativeBufferHelper.readUnsignedInt();
			final int x = nativeBufferHelper.readSignedShort();
			final int y = nativeBufferHelper.readSignedShort();
			final int width = nativeBufferHelper.readUnsignedShort();
			final int height = nativeBufferHelper.readUnsignedShort();
			final int borderWidth = nativeBufferHelper.readUnsignedShort();
			nativeBufferHelper.enableWrite();

			final XWindowGeometry wg = new XWindowGeometry(x, y, width
					+ borderWidth, height + borderWidth, borderWidth);

			return wg;
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative
					.nativeGetCurrentWindowGeometry(getDisplayPeer()
							.longValue(), getArgs()[0].longValue(),
							getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	public FocusWindow getCallFocusWindow() {
		return this.focusWindow;
	}

	private final FocusWindow focusWindow;

	/**
	 * args: (Long) window id, (Integer) time
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class FocusWindow extends
			XNativeCall<Void, Long, Number> {
		private FocusWindow() {
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeGiveFocus(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getNativeBufferHelper()
							.getBuffer());
			return returnboolean;
		}
	}

	/**
	 * FS
	 * 
	 * @return
	 */
	public LowerWindow getCallLowerWindow() {
		return this.lowerWindow;
	}

	private final LowerWindow lowerWindow;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class LowerWindow extends XNativeCall<Void, Long, Long> {
		private LowerWindow() {
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeLower(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public MapWindow getCallMapWindow() {
		return this.mapWindow;
	}

	private final MapWindow mapWindow;

	/**
	 * args: (Long) window id
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class MapWindow extends XNativeCall<Void, Long, Long> {
		private MapWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeMap(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public MoveResizeWindow getCallMoveResizeWindow() {
		return this.moveResizeWindow;
	}

	private final MoveResizeWindow moveResizeWindow;

	/**
	 * args: (Long) window id, (Integer) x, (Integer) y, (Integer) width,
	 * (Integer) height
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class MoveResizeWindow extends
			XNativeCall<Void, Long, Number> {
		private MoveResizeWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeMoveResize(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getArgs()[2].intValue(),
					getArgs()[3].intValue(), getArgs()[4].intValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * 
	 * @return
	 */
	public MoveWindow getCallMoveWindow() {
		return this.moveWindow;
	}

	private final MoveWindow moveWindow;

	/**
	 * args: (Long) window id, (Integer) x, (Integer) y
	 * <p>
	 * return: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class MoveWindow extends
			XNativeCall<Void, Long, Number> {
		private MoveWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeMove(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getArgs()[2].intValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (String) display name
	 */
	public OpenDisplay getCallOpenDisplay() {
		return this.openDisplay;
	}

	private final OpenDisplay openDisplay;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class OpenDisplay extends
			XNativeCall<Long, Long, String> {
		private OpenDisplay() {
		}

		@Override
		public Long getResult() {

			final long displayAddredss = getNativeBufferHelper()
					.readSignedLong();
			getNativeBufferHelper().enableWrite();

			final Long displayAddress = Long.valueOf(displayAddredss);

			return displayAddress;
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeOpenDisplay(
					getArgs()[0], getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) window id, (Boolean) override
	 */
	public OverrideRedirectWindow getCallOverrideRedirectWindow() {
		return this.overrideRedirectWindow;
	}

	private final OverrideRedirectWindow overrideRedirectWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class OverrideRedirectWindow extends
			XNativeCall<Void, Long, Object> {
		private OverrideRedirectWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeOverrideRedirect(
					getDisplayPeer().longValue(),
					((Long) getArgs()[0]).longValue(),
					((Boolean) getArgs()[1]).booleanValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) window id
	 */
	public RaiseWindow getCallRaiseWindow() {
		return this.raiseWindow;
	}

	private final RaiseWindow raiseWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class RaiseWindow extends XNativeCall<Void, Long, Long> {
		private RaiseWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeRaise(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) parent window id, (Long) child window id, (Integer) x,
	 * (Integer) y
	 */
	public ReparentWindow getCallReparentWindow() {
		return this.reparentWindow;
	}

	private final ReparentWindow reparentWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class ReparentWindow extends
			XNativeCall<Void, Long, Number> {
		private ReparentWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeReparent(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getArgs()[1].longValue(), getArgs()[2].intValue(),
					getArgs()[3].intValue(), getNativeBufferHelper()
							.getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) window id, (Integer) width, (Integer) height
	 */
	public ResizeWindow getCallResizeWindow() {
		return this.resizeWindow;
	}

	private final ResizeWindow resizeWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class ResizeWindow extends
			XNativeCall<Void, Long, Number> {
		private ResizeWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeResize(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getArgs()[1].intValue(), getArgs()[2].intValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) window id
	 */
	public SaveYourselfWindow getCallSaveYourselfWindow() {
		return this.saveYourselfWindow;
	}

	private final SaveYourselfWindow saveYourselfWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class SaveYourselfWindow extends
			XNativeCall<Void, Long, Long> {
		private SaveYourselfWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeSaveYourself(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					true, getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: NONE
	 */
	public ShutdownDisplay getCallShutdownDisplay() {
		return this.shutdownDisplay;
	}

	private final ShutdownDisplay shutdownDisplay;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class ShutdownDisplay extends
			XNativeCall<Void, Long, Void> {
		private ShutdownDisplay() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeShutDown(
					getDisplayPeer().longValue(), getNativeBufferHelper()
							.getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: (Long) window id
	 */
	public UnmapWindow getCallUnmapWindow() {
		return this.unmapWindow;
	}

	private final UnmapWindow unmapWindow;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UnmapWindow extends XNativeCall<Void, Long, Long> {
		private UnmapWindow() {
		}

		@Override
		public boolean nativeCallImpl() {

			final boolean returnboolean = XCoreNative.nativeUnmap(
					getDisplayPeer().longValue(), getArgs()[0].longValue(),
					getNativeBufferHelper().getBuffer());

			return returnboolean;
		}
	}

	/**
	 * args: NONE
	 */
	public UpdateXMousePointer getCallUpdateXMousePointer() {
		return this.updateXMousePointer;
	}

	private final UpdateXMousePointer updateXMousePointer;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UpdateXMousePointer extends
			XNativeCall<NativeBufferHelper, Long, Void> {
		private UpdateXMousePointer() {
		}

		@Override
		public NativeBufferHelper getResult() {
			final NativeBufferHelper returnNativeBufferHelper = getNativeBufferHelper();
			return returnNativeBufferHelper;
		}

		@Override
		public boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeGetPointerInfo(
					getDisplayPeer().longValue(), getNativeBufferHelper()
							.getBuffer());
			return returnboolean;
		}
	}

	/**
	 * args: (Long) keySymsPeer, (Integer) keyCode, (Integer) keyColumn
	 * 
	 * ret: (Long) keySymbol
	 */
	public GetKeySym getCallGetKeySym() {
		return this.getKeySym;
	}

	private final GetKeySym getKeySym;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetKeySym extends XNativeCall<Long, Void, Number> {
		private GetKeySym() {
		}

		@Override
		public Long getResult() {
			final long keySymCode = getNativeBufferHelper().readUnsignedInt();
			getNativeBufferHelper().enableWrite();
			return Long.valueOf(keySymCode);
		}

		@Override
		protected boolean nativeCallImpl() {
			final long keysymsPeer = getArgs()[0].longValue();
			final int keyCode = getArgs()[1].intValue();
			final int keyColumn = getArgs()[2].intValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();
			final boolean returnboolean = XCoreNative.nativeGetKeySymbol(
					keysymsPeer, keyCode, keyColumn, buffer);
			return returnboolean;
		}
	}

	/**
	 * args: (Long) keySymsPeer, (Long) keySymbolCode
	 * 
	 * ret: (Integer[]) keyCodes
	 */
	public GetKeyCodes getCallGetKeyCodes() {
		return this.getKeyCodes;
	}

	private final GetKeyCodes getKeyCodes;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetKeyCodes extends
			XNativeCall<Integer[], Void, Long> {
		private GetKeyCodes() {
		}

		@Override
		public Integer[] getResult() {
			final int size = (int) getNativeBufferHelper().readUnsignedInt();
			final Integer[] keyCodes = new Integer[size];
			for (int i = 0; i < size; i++) {
				keyCodes[i] = Integer.valueOf(getNativeBufferHelper()
						.readUnsignedByte());
			}
			getNativeBufferHelper().enableWrite();
			return keyCodes;
		}

		@Override
		protected boolean nativeCallImpl() {
			final long keysymsPeer = getArgs()[0].longValue();
			final long keysymCode = getArgs()[1].longValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();
			final boolean returnboolean = XCoreNative.nativeGetKeyCodes(
					keysymsPeer, keysymCode, buffer);
			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Integer) keyCode, (Integer) modifiersMask
	 * 
	 * ret: void
	 */
	public GrabKey getCallGrabKey() {
		return this.grabKey;
	}

	private final GrabKey grabKey;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GrabKey extends XNativeCall<Void, Long, Number> {
		private GrabKey() {
		}

		@Override
		protected boolean nativeCallImpl() {
			final long displayPeer = getDisplayPeer().longValue();
			final long windowId = getArgs()[0].longValue();
			final short keyCode = getArgs()[1].shortValue();
			final int modifiersMask = getArgs()[2].intValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();

			final boolean returnboolean = XCoreNative.nativeGrabKey(
					displayPeer, windowId, keyCode, modifiersMask, buffer);

			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Integer) buttonCode, (Integer) modifiersMask
	 * 
	 * ret: void
	 */
	public GrabButton getCallGrabButton() {
		return this.grabButton;
	}

	private final GrabButton grabButton;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GrabButton extends
			XNativeCall<Void, Long, Number> {
		private GrabButton() {
		}

		@Override
		protected boolean nativeCallImpl() {

			final long displayPeer = getDisplayPeer().longValue();
			final long windowId = getArgs()[0].longValue();
			final int buttonCode = getArgs()[1].intValue();
			final int modifiersMask = getArgs()[2].intValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();

			final boolean returnboolean = XCoreNative.nativeGrabButton(
					displayPeer, windowId, buttonCode, modifiersMask, buffer);

			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Integer) buttonCode, (Integer) modifiersMask
	 * 
	 * ret: void
	 */
	public UngrabButton getCallUngrabButton() {
		return this.ungrabButton;
	}

	private final UngrabButton ungrabButton;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UngrabButton extends
			XNativeCall<Void, Long, Number> {
		private UngrabButton() {
		}

		@Override
		protected boolean nativeCallImpl() {

			final long displayPeer = getDisplayPeer().longValue();
			final long windowId = getArgs()[0].longValue();
			final int buttonCode = getArgs()[1].intValue();
			final int modifiersMask = getArgs()[2].intValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();

			final boolean returnboolean = XCoreNative.nativeUngrabButton(
					displayPeer, windowId, buttonCode, modifiersMask, buffer);

			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Integer) keyCode, (Integer) modifiersMask
	 * 
	 * ret:void
	 */
	public UngrabKey getCallUngrabKey() {
		return this.ungrabKey;
	}

	private final UngrabKey ungrabKey;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UngrabKey extends XNativeCall<Void, Long, Number> {
		private UngrabKey() {
		}

		@Override
		protected boolean nativeCallImpl() {
			final long displayPeer = getDisplayPeer().longValue();
			final long windowId = getArgs()[0].longValue();
			final short keyCode = getArgs()[1].shortValue();
			final int modifiersMask = getArgs()[2].intValue();
			final ByteBuffer buffer = getNativeBufferHelper().getBuffer();
			final boolean returnboolean = XCoreNative.nativeUngrabKey(
					displayPeer, windowId, keyCode, modifiersMask, buffer);

			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Long( xAtomPropertyId, (Long) xAtomTypeId,
	 * (Integer) dataFormat, (byte[]) data
	 * 
	 * ret: void
	 */
	public ChangeProperty getCallChangeProperty() {
		return this.changeProperty;
	}

	private final ChangeProperty changeProperty;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class ChangeProperty extends
			XNativeCall<Void, Long, Object> {
		private ChangeProperty() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeChangeProperty(getDisplayPeer()
					.longValue(), ((Long) getArgs()[0]).longValue(),
					((Long) getArgs()[1]).longValue(), ((Long) getArgs()[2])
							.longValue(), ((Integer) getArgs()[3]).intValue(),
					(byte[]) getArgs()[4], getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * args:(String) xAtomName
	 * 
	 * ret: (Long) atomId
	 */
	public InternAtom getCallInternAtom() {
		return this.internAtom;
	}

	private final InternAtom internAtom;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class InternAtom extends
			XNativeCall<Long, Long, String> {
		private InternAtom() {
		}

		@Override
		public Long getResult() {
			final Long atomId = Long.valueOf(getNativeBufferHelper()
					.readUnsignedInt());
			getNativeBufferHelper().enableWrite();
			return atomId;
		}

		@Override
		protected boolean nativeCallImpl() {
			final boolean returnboolean = XCoreNative.nativeRegisterAtom(
					getDisplayPeer().longValue(), getArgs()[0],
					getNativeBufferHelper().getBuffer());
			return returnboolean;
		}
	}

	/**
	 * args: (Long) windowId, (Long) atomId, (Integer)format, (Byte[]) data
	 * 
	 * 
	 * ret: void
	 */
	public SendClientMessage getCallSendClientMessage() {
		return this.sendClientMessage;
	}

	private final SendClientMessage sendClientMessage;

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class SendClientMessage extends
			XNativeCall<Void, Long, Object> {
		private SendClientMessage() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeSendClientMessage(getDisplayPeer()
					.longValue(), ((Long) getArgs()[0]).longValue(),
					((Long) getArgs()[1]).longValue(), ((Integer) getArgs()[2])
							.intValue(), (byte[]) getArgs()[3],
					getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public GrabKeyboard getCallGrabKeyboard() {
		return this.grabKeyboard;
	}

	private final GrabKeyboard grabKeyboard;

	/**
	 * args: (Long) windowId, (Integer) time
	 * <p>
	 * ret: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GrabKeyboard extends
			XNativeCall<Void, Long, Number> {
		private GrabKeyboard() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeGrabKeyboard(getDisplayPeer().longValue(),
					getArgs()[0].longValue(), getArgs()[1].intValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public GrabMouse getCallGrabMouse() {
		return this.grabMouse;
	}

	private final GrabMouse grabMouse;

	/**
	 * args: (Long) windowId, (Integer) time
	 * <p>
	 * ret: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GrabMouse extends XNativeCall<Void, Long, Number> {
		private GrabMouse() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeGrabMouse(getDisplayPeer().longValue(),
					getArgs()[0].longValue(), getArgs()[1].intValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public UngrabKeyboard getCallUngrabKeyboard() {
		return this.ungrabKeyboard;
	}

	private final UngrabKeyboard ungrabKeyboard;

	/**
	 * args: (Integer) time
	 * 
	 * ret: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UngrabKeyboard extends
			XNativeCall<Void, Long, Integer> {
		private UngrabKeyboard() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeUngrabKeyboard(getDisplayPeer()
					.longValue(), getArgs()[0].intValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public UngrabMouse getCallUngrabMouse() {
		return this.ungrabMouse;
	}

	private final UngrabMouse ungrabMouse;

	/**
	 * args: (Integer) time
	 * <p>
	 * ret: (Void) null
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class UngrabMouse extends
			XNativeCall<Void, Long, Integer> {
		private UngrabMouse() {
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeUngrabMouse(getDisplayPeer().longValue(),
					getArgs()[0].intValue(), getNativeBufferHelper()
							.getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetProperty getCallGetProperty() {
		return this.getProperty;
	}

	private final GetProperty getProperty;

	/**
	 * args: (Long) window id, (Long) property atom id
	 * <p>
	 * ret: (NativeBufferHelper) property contents buffer
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetProperty extends
			XNativeCall<NativeBufferHelper, Long, Long> {
		private GetProperty() {
		}

		@Override
		public NativeBufferHelper getResult() {
			return getNativeBufferHelper();
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeGetProperty(getDisplayPeer().longValue(),
					getArgs()[0].longValue(), getArgs()[1].longValue(),
					getNativeBufferHelper().getBuffer());
		}
	}

	/**
	 * 
	 * @return
	 */
	public GetInputFocus getCallGetInputFocus() {
		return this.getInputFocus;
	}

	private final GetInputFocus getInputFocus;

	/**
	 * args: (Void) NONE
	 * <p>
	 * return: (Long) focus window id
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static final class GetInputFocus extends
			XNativeCall<Long, Long, Void> {
		private GetInputFocus() {
		}

		@Override
		public Long getResult() {
			final Long windowId = Long.valueOf(getNativeBufferHelper()
					.readUnsignedInt());
			return windowId;
		}

		@Override
		protected boolean nativeCallImpl() {
			return XCoreNative.nativeGetInputFocus(
					getDisplayPeer().longValue(), getNativeBufferHelper()
							.getBuffer());
		}
	}

	/**
	 * Create a new <code>XcbCoreNativeCalls</code> and initialize all needed
	 * <code>XNativeCall</code>s.
	 */
	public XcbCoreNativeCalls() {
		this.translateCoordinates = new TranslateCoordinates();
		this.removeFromSaveSet = new RemoveFromSaveSet();
		this.addToSaveSet = new AddToSaveSet();
		this.setSelectionOwner = new SetSelectionOwner();
		this.getSelectionOwner = new GetSelectionOwner();
		this.createNewWindow = new CreateNewWindow();
		this.ungrabMouse = new UngrabMouse();
		this.getInputFocus = new GetInputFocus();
		this.ungrabKeyboard = new UngrabKeyboard();
		this.grabMouse = new GrabMouse();
		this.grabKeyboard = new GrabKeyboard();
		this.getProperty = new GetProperty();
		this.sendClientMessage = new SendClientMessage();
		this.changeProperty = new ChangeProperty();
		this.internAtom = new InternAtom();
		this.destroyWindow = new DestroyWindow();
		this.enableEvents = new EnableEvents();
		this.flush = new Flush();
		this.getChildWindows = new GetChildWindows();
		this.getNextEvent = new GetNextEvent();
		this.getRootWindow = new GetRootWindow();
		this.getWindowAttributesCopy = new GetWindowAttributesCopy();
		this.getWindowGeometry = new GetWindowGeometry();
		this.focusWindow = new FocusWindow();
		this.lowerWindow = new LowerWindow();
		this.mapWindow = new MapWindow();
		this.moveResizeWindow = new MoveResizeWindow();
		this.moveWindow = new MoveWindow();
		this.openDisplay = new OpenDisplay();
		this.overrideRedirectWindow = new OverrideRedirectWindow();
		this.raiseWindow = new RaiseWindow();
		this.reparentWindow = new ReparentWindow();
		this.resizeWindow = new ResizeWindow();
		this.saveYourselfWindow = new SaveYourselfWindow();
		this.shutdownDisplay = new ShutdownDisplay();
		this.unmapWindow = new UnmapWindow();
		this.updateXMousePointer = new UpdateXMousePointer();
		this.getKeySym = new GetKeySym();
		this.getKeyCodes = new GetKeyCodes();
		this.grabKey = new GrabKey();
		this.grabButton = new GrabButton();
		this.ungrabKey = new UngrabKey();
		this.ungrabButton = new UngrabButton();
	}
}
