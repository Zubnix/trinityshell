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
import org.hydrogen.api.display.event.ClientMessageEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetCurrentDesktopMessage extends EwmhClientMessageEvent {

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	private final int newIndex;
	private final int timestamp;

	/**
	 * 
	 * @param clientMessageEvent
	 */
	public _NetCurrentDesktopMessage(final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		this.newIndex = intDataContainer.readDataBlock().intValue();
		this.timestamp = intDataContainer.readDataBlock().intValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getNewIndex() {
		return this.newIndex;
	}

	/**
	 * 
	 * @return
	 */
	public int getTimestamp() {
		return this.timestamp;
	}

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetCurrentDesktopMessage.TYPE;
	}

}
