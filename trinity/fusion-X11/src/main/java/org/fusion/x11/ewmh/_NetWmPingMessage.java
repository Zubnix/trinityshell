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
package org.fusion.x11.ewmh;

import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.hydrogen.display.api.event.ClientMessageEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmPingMessage extends EwmhClientMessageEvent {

	private final XAtom netWmPingAtom;
	private final int timestamp;
	private final XWindow window;

	/**
	 * 
	 * @param display
	 * @param clientMessageEvent
	 * 
	 */
	public _NetWmPingMessage(final XDisplay display,
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		final Long atomId = Long.valueOf(intDataContainer.readDataBlock()
				.longValue());
		this.netWmPingAtom = display.getDisplayAtoms().getById(atomId);
		this.timestamp = intDataContainer.readDataBlock().intValue();
		final Long windowId = Long.valueOf(intDataContainer.readDataBlock()
				.longValue());
		this.window = display
				.getDisplayPlatform()
				.getResourcesRegistry()
				.getClientXWindow(
						new XID(display, XResourceHandle.valueOf(windowId)));
	}

	/**
	 * 
	 * @return
	 */
	public XAtom getNetWmPingAtom() {
		return this.netWmPingAtom;
	}

	/**
	 * 
	 * @return
	 */
	public int getTimestamp() {
		return this.timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public XWindow getWindow() {
		return this.window;
	}

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetDesktopGeometryMessage.TYPE;
	}

}
