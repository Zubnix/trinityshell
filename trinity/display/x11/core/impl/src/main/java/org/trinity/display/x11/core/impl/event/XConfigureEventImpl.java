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
package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XConfigureEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XConfigureEventImpl implements XConfigureEvent {

	private final int eventCode;
	private final int sequence;
	private final XWindow event;
	private final XWindow window;
	private final XWindow aboveSibling;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int borderWidth;
	private final boolean overrideRedirect;

	@Inject
	public XConfigureEventImpl(	@Assisted("eventCode") final int eventCode,
								@Assisted("sequence") final int sequence,
								@Assisted("event") final XWindow event,
								@Assisted("window") final XWindow window,
								@Assisted("aboveSibling") final XWindow aboveSibling,
								@Assisted("x") final int x,
								@Assisted("y") final int y,
								@Assisted("width") final int width,
								@Assisted("height") final int height,
								@Assisted("borderWidth") final int borderWidth,
								@Assisted final boolean overrideRedirect) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.event = event;
		this.window = window;
		this.aboveSibling = aboveSibling;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.borderWidth = borderWidth;
		this.overrideRedirect = overrideRedirect;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XEvent#getEventCode()
	 */
	@Override
	public int getEventCode() {
		return this.eventCode;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getEvent()
	 */
	@Override
	public XWindow getEvent() {
		return this.event;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {
		return this.window;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getAboveSibling()
	 */
	@Override
	public XWindow getAboveSibling() {
		return this.aboveSibling;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getY()
	 */
	@Override
	public int getY() {
		return this.y;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getwidth()
	 */
	@Override
	public int getwidth() {
		return this.width;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getHeight()
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureEvent#getBorderWidth()
	 */
	@Override
	public int getBorderWidth() {
		return this.borderWidth;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureEvent#isOVerrideRedirect()
	 */
	@Override
	public boolean isOVerrideRedirect() {
		return this.overrideRedirect;
	}

}
