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
import org.trinity.display.x11.core.api.event.XPropertyEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 * 
 ****************************************/
public class XPropertyEventImpl implements XPropertyEvent {

	private final int eventCode;
	private final int sequence;
	private final XWindow window;
	private final XAtom atom;
	private final int time;
	private final boolean state;

	@Inject
	public XPropertyEventImpl(	@Assisted("eventCode") final int eventCode,
								@Assisted("sequence") final int sequence,
								@Assisted final XWindow window,
								@Assisted final XAtom atom,
								@Assisted("time") final int time,
								@Assisted final boolean state) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.window = window;
		this.atom = atom;
		this.time = time;
		this.state = state;
	}

	@Override
	public int getEventCode() {

		return this.eventCode;
	}

	@Override
	public int getSequence() {

		return this.sequence;
	}

	@Override
	public XWindow getWindow() {
		return this.window;
	}

	@Override
	public XAtom getAtom() {
		return this.atom;
	}

	@Override
	public int getTime() {
		return this.time;
	}

	@Override
	public boolean getState() {
		return this.state;
	}

}
