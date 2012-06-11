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

import org.trinity.display.x11.api.XConnection;
import org.trinity.display.x11.api.XEventBridge;
import org.trinity.display.x11.api.event.XEvent;
import org.trinity.display.x11.impl.XCallerImpl;
import org.trinity.display.x11.impl.xcb.displaycall.GetNextEvent;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;

import com.google.inject.Inject;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XcbEventPump implements Runnable {

	private final XCallerImpl xCaller;
	private final XConnection<Long> xConnection;
	private final GetNextEvent getNextEvent;
	private final XEventBridge<NativeBufferHelper> xEventBridge;

	@Inject
	public XcbEventPump(final XCallerImpl xCaller,
						final XConnection<Long> xConnection,
						final GetNextEvent getNextEvent,
						final XEventBridge<NativeBufferHelper> xEventBridge) {
		this.xCaller = xCaller;
		this.xConnection = xConnection;
		this.getNextEvent = getNextEvent;
		this.xEventBridge = xEventBridge;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			final NativeBufferHelper nativeBufferHelper = this.xCaller
					.doCall(this.getNextEvent,
							this.xConnection.getConnectionReference());
			final XEvent xEvent = this.xEventBridge
					.constructXEvent(nativeBufferHelper);
			// TODO handle event in display infrastructure

			this.xEventBridge.queueXEvent(xEvent);
			Thread.yield();
		}
	}
}
