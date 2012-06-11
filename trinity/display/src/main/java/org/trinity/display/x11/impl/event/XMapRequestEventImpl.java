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
package org.trinity.display.x11.impl.event;

import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XMapRequestEvent;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XMapRequestEventImpl implements XMapRequestEvent {

	private final int eventCode;
	private final int sequence;
	private final XWindow parent;
	private final XWindow window;

	/*****************************************
	 * 
	 ****************************************/
	public XMapRequestEventImpl(final int eventCode,
								final int sequence,
								final XWindow parent,
								final XWindow window) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.parent = parent;
		this.window = window;
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
	 * @see org.trinity.display.x11.api.event.XMapRequestEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XMapRequestEvent#getParent()
	 */
	@Override
	public XWindow getParent() {
		return this.parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XMapRequestEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {
		return this.window;
	}
}