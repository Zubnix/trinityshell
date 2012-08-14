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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import xcbcustom.LibXcbLoader;
import xcbjb.LibXcb;
import xcbjb.LibXcbConstants;
import xcbjb.xcb_screen_iterator_t;
import xcbjb.xcb_screen_t;
import xcbjb.xcb_setup_t;
import xcbjb.xcb_window_class_t;

@RunWith(MockitoJUnitRunner.class)
public class XWindowTest // extends AbstractXTest
{

	private XWindow xWindow;
	private int windowId;

	private static final String displayName = ":99";
	private static final int screenNr = 0;

	private static Process xvfb;

	public static XConnection xConnection;

	public static xcb_screen_t screen;

	@BeforeClass
	public static void startup() throws IOException, SecurityException,
			NoSuchFieldException {
		LibXcbLoader.load();

		xvfb = new ProcessBuilder("Xvfb", "-ac", displayName).start();

		xConnection = new XConnection();
		xConnection.open(displayName, screenNr);

		final xcb_setup_t setup = LibXcb.xcb_get_setup(xConnection
				.getConnectionReference());
		final xcb_screen_iterator_t iter = LibXcb
				.xcb_setup_roots_iterator(setup);
		screen = iter.getData();
	}

	@AfterClass
	public static void closedown() {

		xConnection.close();
		xvfb.destroy();
	}

	// mocks
	@Mock
	private XDisplayServer xDisplayServer;

	@Before
	public void setup() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {

		this.windowId = LibXcb.xcb_generate_id(xConnection
				.getConnectionReference());
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4)
				.order(ByteOrder.nativeOrder());
		LibXcb.xcb_create_window(	xConnection.getConnectionReference(),
									(short) LibXcbConstants.XCB_COPY_FROM_PARENT,
									this.windowId,
									screen.getRoot(),
									(short) 0,
									(short) 0,
									200,
									200,
									5,
									xcb_window_class_t.XCB_WINDOW_CLASS_INPUT_OUTPUT
											.swigValue(),
									screen.getRoot_visual(),
									0,
									value_list);

		final XResourceHandle xResourceHandle = new XResourceHandle();

		this.xWindow = new XWindow(	this.xDisplayServer,
									xConnection,
									xResourceHandle);
	}

	@After
	public void teardown() {
		// destroy xwindow instance
		LibXcb.xcb_destroy_window(	xConnection.getConnectionReference(),
									this.windowId);
	}

	@Test
	public void testDestroy() {
		this.xWindow.destroy();
		// TODO verify
	}

	@Test
	public void testSetInputFocus() {
		this.xWindow.setInputFocus();
		// TODO verify
	}

	@Test
	public void testLower() {
		this.xWindow.lower();
	}

	@Test
	public void testShow() {
		this.xWindow.show();
		// TODO verify
	}

	@Test
	public void testMove() {
		this.xWindow.move(10, 10);
		// TODO verify
	}
}