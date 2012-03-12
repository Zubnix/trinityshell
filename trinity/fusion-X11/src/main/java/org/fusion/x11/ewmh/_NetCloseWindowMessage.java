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
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XResourceHandle;
import org.fusion.x11.core.XWindow;
import org.hydrogen.api.display.event.ClientMessageEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetCloseWindowMessage extends EwmhClientMessageEvent {

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	private final int timestamp;
	private final XWindow clientToClose;

	/**
	 * 
	 * @param display
	 * @param clientMessageEvent
	 * 
	 */
	public _NetCloseWindowMessage(final XDisplay display,
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		this.timestamp = intDataContainer.readDataBlock().intValue();
		final Integer xWindowId = intDataContainer.readDataBlock();
		final XID xid = new XID(display, XResourceHandle.valueOf(Long
				.valueOf(xWindowId.longValue())));
		this.clientToClose = display.getDisplayPlatform()
				.getResourcesRegistry().getClientXWindow(xid);
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
	public XWindow getClientToClose() {
		return this.clientToClose;
	}

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetCloseWindowMessage.TYPE;
	}

}
