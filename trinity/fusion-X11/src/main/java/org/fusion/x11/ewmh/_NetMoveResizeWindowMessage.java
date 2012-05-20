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
import org.fusion.x11.core.XGravity;
import org.hydrogen.display.api.event.ClientMessageEvent;

//TODO documentation
/**
* 
* @author Erik De Rijcke
* @since 1.0
*/
public final class _NetMoveResizeWindowMessage extends EwmhClientMessageEvent
		implements HasSourceIndication {

	private final XGravity gravity;
	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureWidth;
	private final boolean configureHeight;

	private final SourceIndication sourceIndication;

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	@SuppressWarnings("deprecation")
	public _NetMoveResizeWindowMessage(
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		final int gravityAndFlags = intDataContainer.readDataBlock().intValue();

		final int gravityCode = gravityAndFlags & 0x000F;
		this.gravity = XGravity.findGravity(gravityCode);

		this.configureX = (gravityAndFlags & (1 << 9)) != 0;
		this.configureY = (gravityAndFlags & (1 << 10)) != 0;
		this.configureWidth = (gravityAndFlags & (1 << 11)) != 0;
		this.configureHeight = (gravityAndFlags & (1 << 12)) != 0;

		final boolean fromClient = (gravityAndFlags & (1 << 13)) != 0;
		final boolean fromPager = (gravityAndFlags & (1 << 14)) != 0;

		if (fromClient) {
			this.sourceIndication = SourceIndication.Application;
		} else if (fromPager) {
			this.sourceIndication = SourceIndication.Pager;
		} else {
			this.sourceIndication = SourceIndication.None;
		}

		this.x = intDataContainer.readDataBlock().intValue();
		this.y = intDataContainer.readDataBlock().intValue();
		this.width = intDataContainer.readDataBlock().intValue();
		this.height = intDataContainer.readDataBlock().intValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * 
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * 
	 * @return
	 */
	public XGravity getGravity() {
		return this.gravity;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConfigureX() {
		return this.configureX;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConfigureY() {
		return this.configureY;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConfigureWidth() {
		return this.configureWidth;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isConfigureHeight() {
		return this.configureHeight;
	}

	@Override
	public SourceIndication getSourceIndication() {
		return this.sourceIndication;
	}

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetDesktopGeometryMessage.TYPE;
	}
}
