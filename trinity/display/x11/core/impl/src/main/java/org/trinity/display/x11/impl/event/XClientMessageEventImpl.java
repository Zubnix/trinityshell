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
import org.trinity.display.x11.api.core.event.XClientMessageEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XClientMessageEventImpl implements XClientMessageEvent {

	private final int eventCode;
	private final int format;
	private final int sequence;
	private final XWindow window;
	private final XAtom atom;
	private final byte[] data;

	@Inject
	public XClientMessageEventImpl(	@Assisted("eventCode") final int eventCode,
									@Assisted("format") final int format,
									@Assisted("sequence") final int sequence,
									@Assisted final XWindow window,
									@Assisted final XAtom atom,
									@Assisted final byte[] data) {
		this.eventCode = eventCode;
		this.format = format;
		this.sequence = sequence;
		this.window = window;
		this.atom = atom;
		this.data = data;
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
	public int getFormat() {
		return this.format;
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
	public byte[] getData() {
		return this.data;
	}
}