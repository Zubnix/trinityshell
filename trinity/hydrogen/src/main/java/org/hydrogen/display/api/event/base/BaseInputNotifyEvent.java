/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.InputNotifyEvent;
import org.hydrogen.display.api.input.Input;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * A <code>BaseInputNotifyEvent</code> is a basic implementation of a
 * <code>InputNotifyEvent</code>. Classes wishing to implement
 * <code>InputNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseInputNotifyEvent<I extends Input> extends BaseDisplayEvent
		implements InputNotifyEvent<I> {

	private final I input;

	/**
	 * @param eventType
	 * @param eventSource
	 * @param input
	 */
	@Inject
	protected BaseInputNotifyEvent(	final DisplayEventType eventType,
									@Assisted final DisplayEventSource eventSource,
									@Assisted final I input) {
		super(eventType, eventSource);
		this.input = input;
	}

	@Override
	public I getInput() {
		return this.input;
	}

	@Override
	public String toString() {
		return String.format(	"%s\tDetails: momentum: %s",
								super.toString(),
								getInput().getMomentum().name());
	}
}
