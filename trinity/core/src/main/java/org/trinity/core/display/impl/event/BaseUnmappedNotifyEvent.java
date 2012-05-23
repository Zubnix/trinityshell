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
package org.trinity.core.display.impl.event;

import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.UnmappedNotifyEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>BaseUnmappedNotifyEvent</code> is a basic implementation of a
 * <code>UnmappedNotifyEvent</code>. Classes wishing to implement
 * <code>UnmappedNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseUnmappedNotifyEvent extends BaseDisplayEvent implements
		UnmappedNotifyEvent {

	/**
	 * @param eventSource
	 */
	@Inject
	protected BaseUnmappedNotifyEvent(	@Named("UnmapNotify") final DisplayEventType displayEventType,
										@Assisted final DisplayEventSource eventSource) {
		super(displayEventType, eventSource);
	}
}
