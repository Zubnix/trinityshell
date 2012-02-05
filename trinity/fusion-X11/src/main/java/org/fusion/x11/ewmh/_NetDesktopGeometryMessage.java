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
import org.hydrogen.displayinterface.event.ClientMessageEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetDesktopGeometryMessage extends EwmhClientMessageEvent {

	private final int newWidth;
	private final int newHeight;

	/**
	 * 
	 * @param clientMessageEvent
	 */
	public _NetDesktopGeometryMessage(
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				getClientMessageEvent().getData());
		this.newWidth = intDataContainer.readDataBlock().intValue();
		this.newHeight = intDataContainer.readDataBlock().intValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getNewWidth() {
		return this.newWidth;
	}

	/**
	 * 
	 * @return
	 */
	public int getNewHeight() {
		return this.newHeight;
	}

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetDesktopGeometryMessage.TYPE;
	}

}
