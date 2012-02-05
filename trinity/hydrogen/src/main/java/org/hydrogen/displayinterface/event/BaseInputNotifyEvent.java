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
package org.hydrogen.displayinterface.event;

import org.hydrogen.displayinterface.input.Input;

// TODO documentation
/**
 * A <code>BaseInputNotifyEvent</code> is a basic implementation of a
 * <code>InputNotifyEvent</code>. Classes wishing to implement
 * <code>InputNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public abstract class BaseInputNotifyEvent<I extends Input> extends
		BaseDisplayEvent implements InputNotifyEvent<I> {

	private final I input;

	/**
	 * 
	 * @param eventType
	 * @param eventSource
	 * @param input
	 */
	public BaseInputNotifyEvent(final DisplayEventType eventType,
			final DisplayEventSource eventSource, final I input) {
		super(eventType, eventSource);
		this.input = input;
	}

	@Override
	public I getInput() {
		return this.input;
	}

	@Override
	public String toString() {
		return String.format("%s\tDetails: momentum: %s", super.toString(),
				getInput().getMomentum().name());
	}
}
