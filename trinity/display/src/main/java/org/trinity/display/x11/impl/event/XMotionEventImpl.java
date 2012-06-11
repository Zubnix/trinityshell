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

import org.trinity.display.x11.api.XDisplayServer;
import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XMotionEvent;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XMotionEventImpl implements XMotionEvent {

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XEvent#getEventCode()
	 */
	@Override
	public int getEventCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getSerial()
	 */
	@Override
	public long getSerial() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#sendEvent()
	 */
	@Override
	public boolean sendEvent() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getDisplay()
	 */
	@Override
	public XDisplayServer getDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getRoot()
	 */
	@Override
	public XWindow getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getSubwindow()
	 */
	@Override
	public XWindow getSubwindow() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getTime()
	 */
	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getX()
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getY()
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getXRoot()
	 */
	@Override
	public int getXRoot() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getYRoot()
	 */
	@Override
	public int getYRoot() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#getState()
	 */
	@Override
	public int getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#isHint()
	 */
	@Override
	public byte isHint() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XMotionEvent#sameScreen()
	 */
	@Override
	public boolean sameScreen() {
		// TODO Auto-generated method stub
		return false;
	}

}
