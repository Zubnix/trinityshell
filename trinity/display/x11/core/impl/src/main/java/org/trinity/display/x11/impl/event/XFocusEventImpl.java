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
import org.trinity.display.x11.api.core.event.XFocusEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XFocusEventImpl implements XFocusEvent {

	private final int eventCode;
	private final int detail;
	private final int sequence;
	private final XWindow event;
	private final int mode;

	@Inject
	public XFocusEventImpl(	@Assisted("eventCode") final int eventCode,
							@Assisted("detail") final int detail,
							@Assisted("sequence") final int sequence,
							@Assisted final XWindow event,
							@Assisted("mode") final int mode) {
		this.eventCode = eventCode;
		this.detail = detail;
		this.sequence = sequence;
		this.event = event;
		this.mode = mode;
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
	 * @see org.trinity.display.x11.api.event.XFocusChangeEvent#getDetail()
	 */
	@Override
	public int getDetail() {
		return this.detail;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XFocusChangeEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XFocusChangeEvent#getEvent()
	 */
	@Override
	public XWindow getEvent() {
		return this.event;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XFocusChangeEvent#getMode()
	 */
	@Override
	public int getMode() {
		return this.mode;
	}
}