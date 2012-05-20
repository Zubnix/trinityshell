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

import org.fusion.x11.core.extension.XSyncAlarmNotify;
import org.fusion.x11.core.extension.XSyncAlarmState;
import org.hydrogen.display.api.event.base.BaseDisplayEvent;

// TODO documentation
// currently unused
/**
 * @author Erik De Rijcke
 * @since 1.1
 */
public class XcbXSyncAlarmNotify extends BaseDisplayEvent implements
                XSyncAlarmNotify {

	private final long            counterValue;
	private final long            alarmValue;
	private final XSyncAlarmState alarmState;

	public XcbXSyncAlarmNotify(final XcbXSyncAlarm eventSource,
	                           final long counterValue,
	                           final long alarmValue,
	                           final XSyncAlarmState alarmState) {
		super(XSyncAlarmNotify.TYPE,
		      eventSource);
		this.counterValue = counterValue;
		this.alarmValue = alarmValue;
		this.alarmState = alarmState;
	}

	@Override
	public XcbXSyncAlarm getEventSource() {
		return (XcbXSyncAlarm) super.getEventSource();
	}

	@Override
	public long getCounterValue() {
		return this.counterValue;
	}

	@Override
	public long getAlarmValue() {
		return this.alarmValue;
	}

	@Override
	public XSyncAlarmState getAlarmState() {
		return this.alarmState;
	}
}