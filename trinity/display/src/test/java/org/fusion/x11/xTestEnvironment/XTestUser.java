/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.xTestEnvironment;

import java.awt.AWTException;
import java.awt.Robot;

/**
 * Test framework code. Currently unused.
 * <p>
 * 
 * @author Erik De Rijcke
 * 
 */
public class XTestUser {
	private final Robot inputPeer;

	public XTestUser() {
		Robot tmpInputPeer = null;
		try {
			tmpInputPeer = new Robot();
		} catch (final AWTException e) {
			throw new RuntimeException(e);
		}
		this.inputPeer = tmpInputPeer;
		this.inputPeer.setAutoDelay(10);
	}

	public Robot getInputPeer() {
		return this.inputPeer;
	}
}
