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

import org.trinity.display.x11.api.extension.sync.XSyncAlarmAttributes;
import org.trinity.display.x11.api.extension.sync.XSyncAlarmState;
import org.trinity.display.x11.api.extension.sync.XSyncTrigger;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXSyncAlarmAttributes implements XSyncAlarmAttributes {

	private final XSyncTrigger    trigger;
	private final long            delta;
	private final boolean         events;
	private final XSyncAlarmState state;

	public XcbXSyncAlarmAttributes(final XSyncTrigger trigger,
	                               final long delta,
	                               final boolean events,
	                               final XSyncAlarmState state) {
		this.trigger = trigger;
		this.delta = delta;
		this.events = events;
		this.state = state;
	}

	@Override
	public XSyncTrigger getTrigger() {
		return this.trigger;
	}

	@Override
	public long getDelta() {
		return this.delta;
	}

	@Override
	public boolean events() {
		return this.events;
	}

	@Override
	public XSyncAlarmState getState() {
		return this.state;
	}
}
