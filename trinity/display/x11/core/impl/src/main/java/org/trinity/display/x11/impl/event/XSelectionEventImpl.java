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

import org.trinity.display.x11.api.core.XAtom;
import org.trinity.display.x11.api.core.XWindow;
import org.trinity.display.x11.api.core.event.XSelectionEvent;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XSelectionEventImpl implements XSelectionEvent {

	private final int eventCode;
	private final int sequence;
	private final int time;
	private final XWindow requestor;
	private final XAtom selection;
	private final XAtom target;
	private final XAtom property;

	public XSelectionEventImpl(	final int eventCode,
								final int sequence,
								final int time,
								final XWindow requestor,
								final XAtom selection,
								final XAtom target,
								final XAtom property) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.time = time;
		this.requestor = requestor;
		this.selection = selection;
		this.target = target;
		this.property = property;
	}

	@Override
	public XAtom getProperty() {
		return this.property;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XEvent#getEventCode()
	 */
	@Override
	public int getEventCode() {
		return this.eventCode;
	}

	@Override
	public int getSequence() {
		return this.sequence;
	}

	@Override
	public int getTime() {
		return this.time;
	}

	@Override
	public XWindow getRequestor() {
		return this.requestor;
	}

	@Override
	public XAtom getSelection() {
		return this.selection;
	}

	@Override
	public XAtom getTarget() {
		return this.target;
	}
}