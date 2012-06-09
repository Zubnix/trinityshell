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

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
import org.fusion.x11.core.IntDataContainer;
import org.trinity.core.display.api.event.ClientMessageEvent;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XIDImpl;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.XWindowImpl;

public final class _NetActiveWindowMessage extends EwmhClientMessageEvent
		implements HasSourceIndication {

	public static final EwmhClientMessageEventType TYPE = new EwmhClientMessageEventType();

	private final SourceIndication sourceIndication;
	private final int timestamp;
	private final XWindowImpl newActiveWindow;

	/**
	 * 
	 * @param display
	 * @param clientMessageEvent
	 */
	public _NetActiveWindowMessage(final XServerImpl display,
			final ClientMessageEvent clientMessageEvent) {
		super(clientMessageEvent);
		final IntDataContainer intDataContainer = new IntDataContainer(
				clientMessageEvent.getData());
		this.sourceIndication = SourceIndication.values()[intDataContainer
				.readDataBlock().intValue()];
		this.timestamp = intDataContainer.readDataBlock().intValue();
		final Integer newActiveWindowId = intDataContainer.readDataBlock();
		this.newActiveWindow = (XWindowImpl) display
				.getDisplayPlatform()
				.getResourcesRegistry()
				.get(new XIDImpl(display, XResourceHandleImpl.valueOf(Long
						.valueOf(newActiveWindowId.longValue()))));
	}

	/**
	 * 
	 * @return
	 */
	public XWindowImpl getNewActiveWindow() {
		return this.newActiveWindow;
	}

	@Override
	public EwmhClientMessageEventType getType() {
		return _NetActiveWindowMessage.TYPE;
	}

	@Override
	public SourceIndication getSourceIndication() {
		return this.sourceIndication;
	}

	/**
	 * 
	 * @return
	 */
	public int getTimestamp() {
		return this.timestamp;
	}
}