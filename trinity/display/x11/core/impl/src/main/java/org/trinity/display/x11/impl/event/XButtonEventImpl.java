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
import org.trinity.display.x11.api.core.event.XButtonEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XButtonEventImpl implements XButtonEvent {

	private final int eventCode;
	private final int sequence;
	private final XWindow window;
	private final XWindow root;
	private final XWindow subwindow;
	private final int time;
	private final int x;
	private final int y;
	private final int xRoot;
	private final int yRoot;
	private final int state;
	private final int button;
	private final boolean sameScreen;

	@Inject
	public XButtonEventImpl(@Assisted("eventCode") final int eventCode,
							@Assisted("sequence") final int sequence,
							@Assisted("window") final XWindow window,
							@Assisted("root") final XWindow root,
							@Assisted("subwindow") final XWindow subwindow,
							@Assisted("time") final int time,
							@Assisted("x") final int x,
							@Assisted("y") final int y,
							@Assisted("xRoot") final int xRoot,
							@Assisted("yRoot") final int yRoot,
							@Assisted("state") final int state,
							@Assisted("button") final int button,
							@Assisted final boolean sameScreen) {
		this.eventCode = eventCode;
		this.sequence = sequence;
		this.window = window;
		this.root = root;
		this.subwindow = subwindow;
		this.time = time;
		this.x = x;
		this.y = y;
		this.xRoot = xRoot;
		this.yRoot = yRoot;
		this.state = state;
		this.button = button;
		this.sameScreen = sameScreen;
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
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getSequence()
	 */
	@Override
	public int getSequence() {

		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {
		return this.window;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getRoot()
	 */
	@Override
	public XWindow getRoot() {
		return this.root;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getSubwindow()
	 */
	@Override
	public XWindow getSubwindow() {
		return this.subwindow;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getTime()
	 */
	@Override
	public int getTime() {
		return this.time;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getY()
	 */
	@Override
	public int getY() {
		return this.y;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getXRoot()
	 */
	@Override
	public int getXRoot() {
		return this.xRoot;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getYRoot()
	 */
	@Override
	public int getYRoot() {
		return this.yRoot;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getState()
	 */
	@Override
	public int getState() {
		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#getButton()
	 */
	@Override
	public int getButton() {
		return this.button;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XButtonEvent#sameScreen()
	 */
	@Override
	public boolean sameScreen() {
		return this.sameScreen;
	}

}
