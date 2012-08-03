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

import java.awt.Button;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.display.api.ResourceHandle;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.foundation.shared.geometry.api.ImmutableRectangle;
import org.trinity.foundation.shared.geometry.api.Rectangle;

import xcbjb.LibXcb;
import xcbjb.SWIGTYPE_p_xcb_connection_t;
import xcbjb.xcb_config_window_t;
import xcbjb.xcb_generic_error_t;
import xcbjb.xcb_get_geometry_cookie_t;
import xcbjb.xcb_get_geometry_reply_t;
import xcbjb.xcb_grab_keyboard_cookie_t;
import xcbjb.xcb_input_focus_t;
import xcbjb.xcb_stack_mode_t;
import xcbjb.xcb_translate_coordinates_cookie_t;
import xcbjb.xcb_translate_coordinates_reply_t;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class XWindow implements DisplayRenderArea {

	@Inject
	@Assisted
	private ResourceHandle resourceHandle;

	@Inject
	private XConnection xConnection;
	@Inject
	private XDisplayServer displayServer;

	@Override
	public ResourceHandle getResourceHandle() {
		return this.resourceHandle;
	}

	@Override
	public void destroy() {

		LibXcb.xcb_destroy_window(getConnectionRef(), getWindowId());
	}

	private int getWindowId() {
		final int windowId = ((XResourceHandle) this.resourceHandle)
				.getNativeHandle();
		return windowId;
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		final SWIGTYPE_p_xcb_connection_t connection_t = this.xConnection
				.getConnectionReference();
		return connection_t;
	}

	@Override
	public void setInputFocus() {

		LibXcb.xcb_set_input_focus(	getConnectionRef(),
									(short) xcb_input_focus_t.XCB_INPUT_FOCUS_NONE
											.swigValue(),
									getWindowId(),
									this.displayServer.getTime());

	}

	@Override
	public void lower() {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE
				.swigValue();
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4)
				.order(ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_BELOW
				.swigValue();
		value_list.putInt(xcb_stack_mode_above);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void show() {
		LibXcb.xcb_map_window(getConnectionRef(), getWindowId());
	}

	@Override
	public void move(final int x, final int y) {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X
				.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(8)
				.order(ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X
				.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(16)
				.order(ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y).putInt(width).putInt(height);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void raise() {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE
				.swigValue();
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4)
				.order(ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_ABOVE
				.swigValue();
		value_list.putInt(xcb_stack_mode_above);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void setParent(	final DisplayRenderArea parent,
							final int x,
							final int y) {
		final int parentId = ((XResourceHandle) parent.getResourceHandle())
				.getNativeHandle();
		LibXcb.xcb_reparent_window(	getConnectionRef(),
									getWindowId(),
									parentId,
									(short) x,
									(short) y);

	}

	@Override
	public void resize(final int width, final int height) {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH
				.swigValue()
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT.swigValue();

		final ByteBuffer value_list = ByteBuffer.allocateDirect(8)
				.order(ByteOrder.nativeOrder());
		value_list.putInt(width).putInt(height);

		LibXcb.xcb_configure_window(getConnectionRef(),
									getWindowId(),
									value_mask,
									value_list);
	}

	@Override
	public void hide() {
		LibXcb.xcb_unmap_window(getConnectionRef(), getWindowId());
	}

	@Override
	public Coordinate translateCoordinates(final DisplayRenderArea source,
											final int sourceX,
											final int sourceY) {
		final int sourceId = ((XResourceHandle) source.getResourceHandle())
				.getNativeHandle();
		final xcb_translate_coordinates_cookie_t cookie_t = LibXcb
				.xcb_translate_coordinates(	getConnectionRef(),
											sourceId,
											getWindowId(),
											(short) sourceX,
											(short) sourceY);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_translate_coordinates_reply_t reply = LibXcb
				.xcb_translate_coordinates_reply(	getConnectionRef(),
													cookie_t,
													e);
		checkError(e);
		final int destX = reply.getDst_x();
		final int destY = reply.getDst_y();

		return new Coordinate(destX, destY);
	}

	@Override
	public Rectangle getGeometry() {
		final xcb_get_geometry_cookie_t cookie_t = LibXcb
				.xcb_get_geometry(getConnectionRef(), getWindowId());
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_geometry_reply_t reply = LibXcb
				.xcb_get_geometry_reply(getConnectionRef(), cookie_t, e);
		checkError(e);
		final int width = reply.getWidth();
		final int height = reply.getHeight();
		final int x = reply.getX();
		final int y = reply.getY();

		return new ImmutableRectangle(x, y, width, height);
	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) == 0) {
			throw new RuntimeException("xcb error");
		}
	}

	@Override
	public void catchKeyboardInput(	final Key catchKey,
									final InputModifiers withModifiers) {
		final int keyCode = catchKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersMask();
		LibXcb.xcb_grab_key(getConnectionRef(),
							owner_events,
							getWindowId(),
							modifiers,
							keyCode,
							pointer_mode,
							keyboard_mode);
	}

	@Override
	public void catchAllKeyboardInput() {
		final xcb_grab_keyboard_cookie_t cookie_t = LibXcb
				.xcb_grab_keyboard(	getConnectionRef(),
									owner_events,
									getWindowId(),
									this.displayServer.getTime(),
									pointer_mode,
									keyboard_mode);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		LibXcb.xcb_grab_keyboard_reply(getConnectionRef(), cookie_t, e);
		checkError(e);
	}

	@Override
	public void stopKeyboardInputCatching() {
		LibXcb.xcb_ungrab_keyboard(	getConnectionRef(),
									this.displayServer.getTime());
	}

	@Override
	public void catchMouseInput(final Button catchButton,
								final InputModifiers withModifiers) {
		final int buttonCode = catchButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersMask();
		LibXcb.xcb_grab_button(	getConnectionRef(),
								owner_events,
								getWindowId(),
								event_mask,
								pointer_mode,
								keyboard_mode,
								confine_to,
								cursor,
								buttonCode,
								modifiers);
	}

	@Override
	public void catchAllMouseInput() {
		LibXcb.xcb_grab_pointer(getConnectionRef(),
								owner_events,
								getWindowId(),
								event_mask,
								pointer_mode,
								keyboard_mode,
								confine_to,
								cursor,
								this.displayServer.getTime());
	}

	@Override
	public void stopMouseInputCatching() {
		LibXcb.xcb_ungrab_pointer(	getConnectionRef(),
									this.displayServer.getTime());
	}

	@Override
	public void disableKeyboardInputCatching(	final Key likeKey,
												final InputModifiers withModifiers) {
		final int key = likeKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersMask();
		LibXcb.xcb_ungrab_key(	getConnectionRef(),
								(short) key,
								getWindowId(),
								modifiers);

	}

	@Override
	public void disableMouseInputCatching(	final Button likeButton,
											final InputModifiers withModifiers) {
		final int button = likeButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersMask();
		LibXcb.xcb_ungrab_button(	getConnectionRef(),
									(short) button,
									getWindowId(),
									modifiers);

	}

}
