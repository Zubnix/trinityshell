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
package org.fusion.x11.core.event;

import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XWindow;
import org.hydrogen.display.event.BaseConfigureRequestEvent;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XConfigureRequest extends BaseConfigureRequestEvent {

	/**
	 * 
	 * @param window
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param valueMask
	 */
	public XConfigureRequest(final XWindow window, final int x, final int y,
			final int width, final int height, final int valueMask) {
		super(window, (valueMask & XProtocolConstants.CONFIG_WINDOW_X) != 0,
				(valueMask & XProtocolConstants.CONFIG_WINDOW_Y) != 0,
				(valueMask & XProtocolConstants.CONFIG_WINDOW_WIDTH) != 0,
				(valueMask & XProtocolConstants.CONFIG_WINDOW_HEIGHT) != 0, x,
				y, width, height);
	}

	@Override
	public XWindow getEventSource() {
		return (XWindow) super.getEventSource();
	}

}
