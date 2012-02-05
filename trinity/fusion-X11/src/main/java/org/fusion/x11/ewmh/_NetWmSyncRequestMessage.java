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
import org.hydrogen.displayinterface.event.ClientMessageEvent;

//TODO documentation
/**
* 
* @author Erik De Rijcke
* @since 1.0
*/
public final class _NetWmSyncRequestMessage extends EwmhClientMessageEvent {

	private final XAtom netWmSyncRequest;
	private final int timestamp;
	private final int lowerBitsUpdateRequest;
	private final int higherBitsUpdateRequest;

	/**
	 * 
	 * @param display
	 * @param clientMessageEvent
	 */
	public _NetWmSyncRequestMessage(final XDisplay display,
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		final Long atomId = Long.valueOf(intDataContainer.readDataBlock()
				.longValue());

		this.netWmSyncRequest = display.getDisplayAtoms().getById(atomId);
		this.timestamp = intDataContainer.readDataBlock().intValue();
		this.lowerBitsUpdateRequest = intDataContainer.readDataBlock()
				.intValue();
		this.higherBitsUpdateRequest = intDataContainer.readDataBlock()
				.intValue();
	}

	/**
	 * 
	 * @return
	 */
	public XAtom getNetWmSyncRequest() {
		return this.netWmSyncRequest;
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
	public int getLowerBitsUpdateRequest() {
		return this.lowerBitsUpdateRequest;
	}

	/**
	 * 
	 * @return
	 */
	public int getHigherBitsUpdateRequest() {
		return this.higherBitsUpdateRequest;
	}

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetDesktopGeometryMessage.TYPE;
	}
}
