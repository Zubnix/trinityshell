/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.core.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.foundation.shared.geometry.api.ImmutableRectangle;
import org.trinity.foundation.shared.geometry.api.Rectangle;

import xcbjb.LibXcb;
import xcbjb.LibXcbConstants;
import xcbjb.SWIGTYPE_p_xcb_connection_t;
import xcbjb.xcb_config_window_t;
import xcbjb.xcb_cw_t;
import xcbjb.xcb_event_mask_t;
import xcbjb.xcb_generic_error_t;
import xcbjb.xcb_get_geometry_cookie_t;
import xcbjb.xcb_get_geometry_reply_t;
import xcbjb.xcb_grab_mode_t;
import xcbjb.xcb_input_focus_t;
import xcbjb.xcb_query_pointer_cookie_t;
import xcbjb.xcb_query_pointer_reply_t;
import xcbjb.xcb_stack_mode_t;
import xcbjb.xcb_translate_coordinates_cookie_t;
import xcbjb.xcb_translate_coordinates_reply_t;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XWindow implements DisplaySurface {

	private final DisplaySurfaceHandle resourceHandle;
	private final XConnection xConnection;
	private final XTime xTime;

	@Inject
	XWindow(final XTime xTime, final XConnection xConnection, @Assisted final DisplaySurfaceHandle resourceHandle) {
		this.xTime = xTime;
		this.xConnection = xConnection;
		this.resourceHandle = resourceHandle;
	}

	@Override
	public DisplaySurfaceHandle getDisplaySurfaceHandle() {
		return this.resourceHandle;
	}

	@Override
	public void destroy() {

		LibXcb.xcb_destroy_window(	getConnectionRef(),
									getWindowId());
	}

	private int getWindowId() {
		final int windowId = ((Integer) this.resourceHandle.getNativeHandle()).intValue();
		return windowId;
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		final SWIGTYPE_p_xcb_connection_t connection_t = this.xConnection.getConnectionReference();
		return connection_t;
	}

	@Override
	public void setInputFocus() {

		LibXcb.xcb_set_input_focus(	getConnectionRef(),
									(short) xcb_input_focus_t.XCB_INPUT_FOCUS_NONE.swigValue(),
									getWindowId(),
									this.xTime.getTime());

	}

	@Override
	public void lower() {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE.swigValue();
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_BELOW.swigValue();
		value_list.putInt(xcb_stack_mode_above);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void show() {
		final int windowId = getWindowId();
		LibXcb.xcb_map_window(	getConnectionRef(),
								windowId);
	}

	@Override
	public void move(	final int x,
						final int y) {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y);

		final int windowId = getWindowId();
		LibXcb.xcb_configure_window(getConnectionRef(),
									windowId,
									value_mask,
									value_list);
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y).putInt(width).putInt(height);

		final int windowId = getWindowId();
		LibXcb.xcb_configure_window(getConnectionRef(),
									windowId,
									value_mask,
									value_list);
	}

	@Override
	public void raise() {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE.swigValue();
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_ABOVE.swigValue();
		value_list.putInt(xcb_stack_mode_above);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void setParent(	final DisplaySurface parent,
							final int x,
							final int y) {
		final int parentId = ((Integer) (parent.getDisplaySurfaceHandle()).getNativeHandle()).intValue();
		LibXcb.xcb_reparent_window(	getConnectionRef(),
									getWindowId(),
									parentId,
									(short) x,
									(short) y);

	}

	@Override
	public void resize(	final int width,
						final int height) {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(8).order(ByteOrder.nativeOrder());
		value_list.putInt(width).putInt(height);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void hide() {
		LibXcb.xcb_unmap_window(getConnectionRef(),
								getWindowId());
	}

	@Override
	public Coordinate translateCoordinates(	final DisplaySurface source,
											final int sourceX,
											final int sourceY) {
		final int sourceId = ((Integer) (source.getDisplaySurfaceHandle()).getNativeHandle()).intValue();
		final xcb_translate_coordinates_cookie_t cookie_t = LibXcb.xcb_translate_coordinates(	getConnectionRef(),
																								sourceId,
																								getWindowId(),
																								(short) sourceX,
																								(short) sourceY);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_translate_coordinates_reply_t reply = LibXcb.xcb_translate_coordinates_reply(	getConnectionRef(),
																								cookie_t,
																								e);
		checkError(e);
		final int destX = reply.getDst_x();
		final int destY = reply.getDst_y();

		return new Coordinate(	destX,
								destY);
	}

	@Override
	public Rectangle getGeometry() {
		final xcb_get_geometry_cookie_t cookie_t = LibXcb.xcb_get_geometry(	getConnectionRef(),
																			getWindowId());
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_geometry_reply_t reply = LibXcb.xcb_get_geometry_reply(	getConnectionRef(),
																				cookie_t,
																				e);
		checkError(e);
		final int width = reply.getWidth();
		final int height = reply.getHeight();
		final int x = reply.getX();
		final int y = reply.getY();

		return new ImmutableRectangle(	x,
										y,
										width,
										height);
	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			throw new RuntimeException("xcb error");
		}
	}

	@Override
	public void grabButton(	final Button catchButton,
							final InputModifiers withModifiers) {
		final int buttonCode = catchButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS.swigValue()
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE.swigValue();
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		LibXcb.xcb_grab_button(	getConnectionRef(),
								(short) 0,
								getWindowId(),
								event_mask,
								(short) pointer_mode,
								(short) keyboard_mode,
								confine_to,
								cursor,
								(short) buttonCode,
								modifiers);
	}

	@Override
	public void grabPointer() {
		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS.swigValue()
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE.swigValue();
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		// TODO check if grab was successful and return boolean
		LibXcb.xcb_grab_pointer(getConnectionRef(),
								(short) 0,
								getWindowId(),
								event_mask,
								(short) pointer_mode,
								(short) keyboard_mode,
								confine_to,
								cursor,
								this.xTime.getTime());
	}

	@Override
	public void ungrabPointer() {
		LibXcb.xcb_ungrab_pointer(	this.xConnection.getConnectionReference(),
									this.xTime.getTime());
	}

	@Override
	public void ungrabButton(	final Button likeButton,
								final InputModifiers withModifiers) {
		final int button = likeButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		LibXcb.xcb_ungrab_button(	getConnectionRef(),
									(short) button,
									getWindowId(),
									modifiers);
	}

	@Override
	public void grabKey(final Key catchKey,
						final InputModifiers withModifiers) {
		final int keyCode = catchKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		LibXcb.xcb_grab_key(getConnectionRef(),
							(short) 0,
							getWindowId(),
							modifiers,
							(short) keyCode,
							(short) pointer_mode,
							(short) keyboard_mode);

	}

	@Override
	public void ungrabKey(	final Key catchKey,
							final InputModifiers withModifiers) {
		final int key = catchKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersState();
		LibXcb.xcb_ungrab_key(	getConnectionRef(),
								(short) key,
								getWindowId(),
								modifiers);
	}

	@Override
	public void ungrabKeyboard() {
		LibXcb.xcb_ungrab_keyboard(	getConnectionRef(),
									this.xTime.getTime());
	}

	@Override
	public void grabKeyboard() {
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC.swigValue();
		// TODO check if grab was successful and return boolean
		LibXcb.xcb_grab_keyboard(	getConnectionRef(),
									(short) 0,
									getWindowId(),
									this.xTime.getTime(),
									(short) pointer_mode,
									(short) keyboard_mode);
	}

	@Override
	public Coordinate getPointerCoordinate() {
		final xcb_query_pointer_cookie_t cookie_t = LibXcb.xcb_query_pointer(	getConnectionRef(),
																				getWindowId());
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_query_pointer_reply_t reply_t = LibXcb.xcb_query_pointer_reply(	getConnectionRef(),
																					cookie_t,
																					e);
		checkError(e);
		final int x = reply_t.getWin_x();
		final int y = reply_t.getWin_y();
		return new Coordinate(	x,
								y);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XWindow) {
			final XWindow otherWindow = (XWindow) obj;
			return otherWindow.getDisplaySurfaceHandle().getNativeHandle()
					.equals(getDisplaySurfaceHandle().getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getDisplaySurfaceHandle().getNativeHandle().hashCode();
	}

	public void configureClientEvents() {
		final ByteBuffer values = ByteBuffer.allocateDirect(4 + 4 + 4 + 4).order(ByteOrder.nativeOrder());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY.swigValue());

		LibXcb.xcb_change_window_attributes(this.xConnection.getConnectionReference(),
											((XWindowHandle) this.resourceHandle).getNativeHandle().intValue(),
											xcb_cw_t.XCB_CW_EVENT_MASK.swigValue(),
											values);
	}
}