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

import org.trinity.core.display.api.Property;
import org.trinity.core.display.api.PropertyInstance;
import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.PropertyChangedNotifyEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>BasePropertyChangedNotifyEvent</code> is a basic implementation of a
 * <code>PropertyChangedNotifyEvent</code>. Classes wishing to implement
 * <code>PropertyChangedNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BasePropertyChangedNotifyEvent extends BaseDisplayEvent implements
		PropertyChangedNotifyEvent {

	private final boolean propertyDeleted;
	private final Property<? extends PropertyInstance> changedProperty;

	/**
	 * @param eventSource
	 * @param propertyDeleted
	 * @param changedProperty
	 */
	@Inject
	protected BasePropertyChangedNotifyEvent(	@Named("PropertyChanged") final DisplayEventType displayEventType,
												@Assisted final DisplayEventSource eventSource,
												@Assisted final boolean propertyDeleted,
												@Assisted final Property<? extends PropertyInstance> changedProperty) {
		super(displayEventType, eventSource);
		this.propertyDeleted = propertyDeleted;
		this.changedProperty = changedProperty;
	}

	@Override
	public boolean isPropertyDeleted() {
		return this.propertyDeleted;
	}

	@Override
	public Property<? extends PropertyInstance> getChangedProperty() {
		return this.changedProperty;
	}
}