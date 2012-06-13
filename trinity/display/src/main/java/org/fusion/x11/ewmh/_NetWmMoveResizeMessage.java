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
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XIDImpl;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.XWindowImpl;
import org.trinity.foundation.display.api.event.ClientMessageEvent;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmMoveResizeMessage extends EwmhClientMessageEvent
		implements HasSourceIndication {

	private final int absoluteX;
	private final int absoluteY;
	private final Direction direction;
	private final XWindowImpl button;

	private final SourceIndication sourceIndication;

	/**
	 * 
	 * @param display
	 * @param clientMessageEvent
	 * 
	 */
	public _NetWmMoveResizeMessage(final XServerImpl display,
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		this.absoluteX = intDataContainer.readDataBlock().intValue();
		this.absoluteY = intDataContainer.readDataBlock().intValue();
		final int directionValue = intDataContainer.readDataBlock().intValue();
		this.direction = Direction.values()[directionValue];
		final Integer windowId = intDataContainer.readDataBlock().intValue();
		this.button = display
				.getDisplayPlatform()
				.getResourcesRegistry()
				.getClientXWindow(
						new XIDImpl(display, XResourceHandleImpl.valueOf(Long
								.valueOf(windowId.longValue()))));
		final int source = intDataContainer.readDataBlock().intValue();

		this.sourceIndication = SourceIndication.values()[source];
	}

	/**
	 * 
	 * @return
	 */
	public int getAbsoluteX() {
		return this.absoluteX;
	}

	/**
	 * 
	 * @return
	 */
	public int getAbsoluteY() {
		return this.absoluteY;
	}

	/**
	 * 
	 * @return
	 */
	public Direction getDirection() {
		return this.direction;
	}

	/**
	 * 
	 * @return
	 */
	public XWindowImpl getButton() {
		return this.button;
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
