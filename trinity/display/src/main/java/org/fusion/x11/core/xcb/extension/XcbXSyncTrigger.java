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
package org.fusion.x11.core.xcb.extension;

import org.trinity.display.x11.api.extension.sync.XSyncTestType;
import org.trinity.display.x11.api.extension.sync.XSyncTrigger;
import org.trinity.display.x11.api.extension.sync.XSyncValueType;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXSyncTrigger implements XSyncTrigger {

	private final long           counter;
	private final XSyncValueType valueType;
	private final long           waitValue;
	private final XSyncTestType  testType;

	public XcbXSyncTrigger(final long counter,
	                       final XSyncValueType valueType,
	                       final long waitValue,
	                       final XSyncTestType testType) {
		this.counter = counter;
		this.valueType = valueType;
		this.waitValue = waitValue;
		this.testType = testType;
	}

	@Override
	public long getCounter() {
		return this.counter;
	}

	@Override
	public XSyncValueType getValueType() {
		return this.valueType;
	}

	@Override
	public long getWaitValue() {
		return this.waitValue;
	}

	@Override
	public XSyncTestType getTestType() {
		return this.testType;
	}
}
