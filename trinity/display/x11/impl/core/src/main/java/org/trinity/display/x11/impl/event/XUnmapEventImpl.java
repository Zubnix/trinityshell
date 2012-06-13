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

import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.core.event.XUnmapEvent;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XUnmapEventImpl implements XUnmapEvent {

	private final int eventCode;
	private final int sequence;
	private final XWindow event;
	private final XWindow window;
	private final boolean fromConfigure;

	public XUnmapEventImpl(	@Assisted("eventCode") final int eventCode,
							@Assisted("sequence") final int sequence,
							@Assisted("event") final XWindow event,
							@Assisted("window") final XWindow window,
							@Assisted final boolean fromConfigure) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.event = event;
		this.window = window;
		this.fromConfigure = fromConfigure;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XEvent#getEventCode()
	 */
	@Override
	public int getEventCode() {
		return this.eventCode;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XUnmapEvent#getSequence()
	 */
	@Override
	public int getSequence() {

		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XUnmapEvent#getEvent()
	 */
	@Override
	public XWindow getEvent() {

		return this.event;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XUnmapEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {

		return this.window;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XUnmapEvent#isFromConfigure()
	 */
	@Override
	public boolean isFromConfigure() {

		return this.fromConfigure;
	}
}