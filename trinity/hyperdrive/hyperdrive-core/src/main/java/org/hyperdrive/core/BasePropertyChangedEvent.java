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
package org.hyperdrive.core;

import java.util.HashMap;
import java.util.Map;

import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.core.event.PropertyChangedEvent;
import org.hyperdrive.api.core.event.PropertyChangedEventType;

//TODO create seperate abstract renderareaevent super class
//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BasePropertyChangedEvent<T extends Property<? extends PropertyInstance>>
		implements PropertyChangedEvent<T> {

	private final T changedProperty;
	private final boolean propertyDeleted;
	private final RenderArea renderArea;
	private final PropertyChangedEventType type;
	private static final Map<String, PropertyChangedEventType> TYPES_MAP = new HashMap<String, PropertyChangedEventType>();

	/**
	 * 
	 * @param renderArea
	 * @param propertyDeleted
	 * @param changedProperty
	 */
	public BasePropertyChangedEvent(final RenderArea renderArea,
			final boolean propertyDeleted, final T changedProperty) {
		this.renderArea = renderArea;
		this.type = BasePropertyChangedEvent
				.TYPE(changedProperty.getAtomName());
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
	 * @param propertyName
	 * @return
	 */
	public static PropertyChangedEventType TYPE(final String propertyName) {
		PropertyChangedEventType type;
		if (BasePropertyChangedEvent.TYPES_MAP.containsKey(propertyName)) {
			type = BasePropertyChangedEvent.TYPES_MAP.get(propertyName);
		} else {
			type = new BasePropertyChangedEventType(propertyName);
			BasePropertyChangedEvent.TYPES_MAP.put(propertyName, type);
		}
		return type;
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
	public PropertyChangedEventType getType() {
		return this.type;
	}
}