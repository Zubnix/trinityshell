/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.foundation.api.base;

import org.hydrogen.display.api.Property;
import org.hydrogen.display.api.PropertyInstance;
import org.hyperdrive.foundation.api.RenderArea;
import org.hyperdrive.foundation.api.event.PropertyChangedEvent;
import org.hyperdrive.foundation.api.event.PropertyType;

//TODO create seperate abstract renderareaevent super class
//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
final class BasePropertyChangedEvent<T extends Property<? extends PropertyInstance>>
		implements PropertyChangedEvent<T> {

	private final T changedProperty;
	private final boolean propertyDeleted;
	private final RenderArea renderArea;
	private final PropertyType type;

	/**
	 * 
	 * @param renderArea
	 * @param propertyDeleted
	 * @param changedProperty
	 */
	BasePropertyChangedEvent(final RenderArea renderArea,
			final boolean propertyDeleted, final T changedProperty) {
		// protected to reduce visibility and encourage dependency injection
		this.renderArea = renderArea;
		this.type = PropertyChangedEvent.TYPE
				.get(changedProperty.getAtomName());
		this.propertyDeleted = propertyDeleted;
		this.changedProperty = changedProperty;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public RenderArea getRenderArea() {
		return this.renderArea;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public T getProperty() {
		return this.changedProperty;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isPropertyDeleted() {
		return this.propertyDeleted;
	}

	@Override
	public PropertyType getType() {
		return this.type;
	}
}