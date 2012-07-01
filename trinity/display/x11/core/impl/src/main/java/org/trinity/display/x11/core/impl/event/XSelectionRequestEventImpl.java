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

import org.trinity.display.x11.core.api.XAtom;
import org.trinity.display.x11.core.api.XWindow;
import org.trinity.display.x11.core.api.event.XSelectionRequestEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XSelectionRequestEventImpl implements XSelectionRequestEvent {

	private final int eventCode;
	private final int sequence;
	private final int time;
	private final XWindow owner;
	private final XWindow requestor;
	private final XAtom selection;
	private final XAtom target;
	private final XAtom property;

	@Inject
	public XSelectionRequestEventImpl(	@Assisted("eventCode") final int eventCode,
										@Assisted("sequence") final int sequence,
										@Assisted("time") final int time,
										@Assisted("owner") final XWindow owner,
										@Assisted("requestor") final XWindow requestor,
										@Assisted("selection") final XAtom selection,
										@Assisted("target") final XAtom target,
										@Assisted("property") final XAtom property) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.time = time;
		this.owner = owner;
		this.requestor = requestor;
		this.selection = selection;
		this.target = target;
		this.property = property;
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
	public XWindow getOwner() {
		return this.owner;
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

	@Override
	public XAtom getProperty() {
		return this.property;
	}
}