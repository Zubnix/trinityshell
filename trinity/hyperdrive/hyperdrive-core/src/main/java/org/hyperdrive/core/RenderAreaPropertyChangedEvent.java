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

import org.hydrogen.displayinterface.Property;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.eventsystem.Event;

//TODO create seperate abstract renderareaevent super class
//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class RenderAreaPropertyChangedEvent<T extends Property<? extends PropertyInstance>>
		implements Event<RenderAreaPropertyChangedEventType> {

	private final T changedProperty;
	private final boolean propertyDeleted;
	private final AbstractRenderArea renderArea;
	private final RenderAreaPropertyChangedEventType type;
	private static final Map<String, RenderAreaPropertyChangedEventType> TYPES_MAP = new HashMap<String, RenderAreaPropertyChangedEventType>();

	/**
	 * 
	 * @param renderArea
	 * @param propertyDeleted
	 * @param changedProperty
	 */
	public RenderAreaPropertyChangedEvent(final AbstractRenderArea renderArea,
			final boolean propertyDeleted, final T changedProperty) {
		this.renderArea = renderArea;
		this.type = RenderAreaPropertyChangedEvent.TYPE(changedProperty
				.getAtomName());
		this.propertyDeleted = propertyDeleted;
		this.changedProperty = changedProperty;
	}

	/**
	 * 
	 * @return
	 */
	public AbstractRenderArea getRenderArea() {
		return this.renderArea;
	}

	/**
	 * 
	 * @return
	 */
	public T getChangedProperty() {
		return this.changedProperty;
	}

	/**
	 * 
	 * @param propertyName
	 * @return
	 */
	public static RenderAreaPropertyChangedEventType TYPE(
			final String propertyName) {
		RenderAreaPropertyChangedEventType type;
		if (RenderAreaPropertyChangedEvent.TYPES_MAP.containsKey(propertyName)) {
			type = RenderAreaPropertyChangedEvent.TYPES_MAP.get(propertyName);
		} else {
			type = new RenderAreaPropertyChangedEventType(propertyName);
			RenderAreaPropertyChangedEvent.TYPES_MAP.put(propertyName, type);
		}
		return type;
	}

	/**
	 * 
	 * @return
	 */
	public boolean propertyDeleted() {
		return this.propertyDeleted;
	}

	@Override
	public RenderAreaPropertyChangedEventType getType() {
		return this.type;
	}
}