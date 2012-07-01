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
import org.trinity.display.x11.core.api.event.XSelectionClearEvent;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XSelectionClearEventImpl implements XSelectionClearEvent {

	final int eventCode;
	final int sequence;
	final int time;
	final XWindow owner;
	final XAtom selection;

	/*****************************************
	 * 
	 ****************************************/
	public XSelectionClearEventImpl(@Assisted("eventCode") final int eventCode,
									@Assisted("sequence") final int sequence,
									@Assisted("time") final int time,
									@Assisted final XWindow owner,
									@Assisted final XAtom selection) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.time = time;
		this.owner = owner;
		this.selection = selection;
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
	 * @see org.trinity.display.x11.api.event.XSelectionClearEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XSelectionClearEvent#getTime()
	 */
	@Override
	public int getTime() {
		return this.time;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XSelectionClearEvent#getOwner()
	 */
	@Override
	public XWindow getOwner() {
		return this.owner;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XSelectionClearEvent#getSelection()
	 */
	@Override
	public XAtom getSelection() {
		return this.selection;
	}

}
