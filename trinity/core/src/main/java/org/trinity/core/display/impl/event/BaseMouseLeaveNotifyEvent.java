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
import org.trinity.core.display.api.event.MouseVisitationNotifyEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>BaseMouseLeaveNotifyEvent</code> is a basic implementation of a
 * <code>MouseEnterLeaveNotifyEvent</code>. Classes wishing to implement
 * <code>MouseEnterLeaveNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseMouseLeaveNotifyEvent extends BaseDisplayEvent implements
		MouseVisitationNotifyEvent {

	/**
	 * @param eventSource
	 */
	@Inject
	protected BaseMouseLeaveNotifyEvent(@Named("MouseLeave") final DisplayEventType displayEventType,
										@Assisted final DisplayEventSource eventSource) {
		super(displayEventType, eventSource);
	}
}
