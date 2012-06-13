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
import org.trinity.display.x11.api.core.event.XPointerVisitationEvent;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XPointerVisitationEventImpl implements XPointerVisitationEvent {

	private final int eventCode;
	private final int detail;
	private final int sequence;
	private final int time;
	private final XWindow root;
	private final XWindow event;
	private final XWindow child;
	private final int rootX;
	private final int rootY;
	private final int eventX;
	private final int eventY;
	private final int state;
	private final int mode;
	private final boolean sameScreen;

	public XPointerVisitationEventImpl(	@Assisted("eventCode") final int eventCode,
										@Assisted("detail") final int detail,
										@Assisted("sequence") final int sequence,
										@Assisted("time") final int time,
										@Assisted("root") final XWindow root,
										@Assisted("event") final XWindow event,
										@Assisted("child") final XWindow child,
										@Assisted("rootX") final int rootX,
										@Assisted("rootY") final int rootY,
										@Assisted("eventX") final int eventX,
										@Assisted("eventY") final int eventY,
										@Assisted("state") final int state,
										@Assisted("mode") final int mode,
										@Assisted final boolean sameScreen) {
		this.eventCode = eventCode;
		this.detail = detail;
		this.sequence = sequence;
		this.time = time;
		this.root = root;
		this.event = event;
		this.child = child;
		this.rootX = rootX;
		this.rootY = rootY;
		this.eventX = eventX;
		this.eventY = eventY;
		this.state = state;
		this.mode = mode;
		this.sameScreen = sameScreen;
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
	 * @see
	 * org.trinity.display.x11.api.event.XPointerVisitationEvent#getDetail()
	 */
	@Override
	public int getDetail() {
		return this.detail;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XPointerVisitationEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getTime()
	 */
	@Override
	public int getTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getRoot()
	 */
	@Override
	public XWindow getRoot() {
		// TODO Auto-generated method stub
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getEvent()
	 */
	@Override
	public XWindow getEvent() {
		return this.event;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getChild()
	 */
	@Override
	public XWindow getChild() {
		return this.child;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getRootX()
	 */
	@Override
	public int getRootX() {
		return this.rootX;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getRootY()
	 */
	@Override
	public int getRootY() {
		return this.rootY;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XPointerVisitationEvent#getEventX()
	 */
	@Override
	public int getEventX() {
		return this.eventX;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XPointerVisitationEvent#getEventY()
	 */
	@Override
	public int getEventY() {
		return this.eventY;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getState()
	 */
	@Override
	public int getState() {
		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XPointerVisitationEvent#getMode()
	 */
	@Override
	public int getMode() {
		return this.mode;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XPointerVisitationEvent#isSameScreen()
	 */
	@Override
	public boolean isSameScreen() {
		return this.sameScreen;
	}

}
