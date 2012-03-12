/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.event;

import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;

// TODO documentation
/**
 * A <code>BaseDisplayEvent</code> is a basic implementation of a
 * <code>DisplayEvent</code>. Classes wishing to implement
 * <code>DisplayEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseDisplayEvent implements DisplayEvent {

	private final DisplayEventType eventType;
	private final DisplayEventSource eventSource;

	/**
	 * 
	 * @param eventType
	 * @param eventSource
	 */
	public BaseDisplayEvent(final DisplayEventType eventType,
			final DisplayEventSource eventSource) {
		this.eventType = eventType;
		this.eventSource = eventSource;
	}

	@Override
	public DisplayEventType getType() {
		return this.eventType;
	}

	@Override
	public DisplayEventSource getEventSource() {
		return this.eventSource;
	}

	@Override
	public String toString() {
		final String toString = String.format(
				"Display Event class: <%s> - Event Source: <%s>", getClass()
						.getSimpleName(), getEventSource());
		return toString;
	}

}
