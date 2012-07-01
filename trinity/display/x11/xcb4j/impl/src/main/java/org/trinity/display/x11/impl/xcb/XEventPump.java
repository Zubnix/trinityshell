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
package org.trinity.display.x11.impl.xcb;

import org.trinity.display.x11.core.api.XCall;
import org.trinity.display.x11.core.api.XCaller;
import org.trinity.display.x11.core.api.XConnection;
import org.trinity.display.x11.core.api.XEventBridge;
import org.trinity.display.x11.core.api.event.XEvent;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XEventPump implements Runnable {

	private final XCaller xCaller;
	private final XConnection<Long> xConnection;
	private final XCall<XEvent, Long, Void> getNextEvent;
	private final XEventBridge<NativeBufferHelper> xEventBridge;

	@Inject
	public XEventPump(	final XCaller xCaller,
						final XConnection<Long> xConnection,
						@Named("GetNextEvent") final XCall<XEvent, Long, Void> getNextEvent,
						final XEventBridge<NativeBufferHelper> xEventBridge) {
		this.xCaller = xCaller;
		this.xConnection = xConnection;
		this.getNextEvent = getNextEvent;
		this.xEventBridge = xEventBridge;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			final XEvent xEvent = this.xCaller
					.doCall(this.getNextEvent,
							this.xConnection.getConnectionReference());
			// TODO handle event in display infrastructure

			this.xEventBridge.queueXEvent(xEvent);
			Thread.yield();
		}
	}
}