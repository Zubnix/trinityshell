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

import org.hydrogen.displayinterface.Property;
import org.hydrogen.displayinterface.PropertyInstance;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class RenderAreaPropertiesManipulator {

	private final AbstractRenderArea renderArea;

	/**
	 * 
	 * @param renderArea
	 */
	public RenderAreaPropertiesManipulator(final AbstractRenderArea renderArea) {
		this.renderArea = renderArea;
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
	 * @param propertyName
	 * @return
	 */
	public <T extends PropertyInstance> T getPropertyValue(
			final String propertyName) {
		@SuppressWarnings("unchecked")
		final Property<T> property = (Property<T>) getRenderArea()
				.getManagedDisplay().getDisplay().getDisplayAtoms()
				.getAtomByName(propertyName);
		T propertyInstance = null;
		propertyInstance = getRenderArea().getPlatformRenderArea()
				.getPropertyInstance(property);

		return propertyInstance;
	}

	/**
	 * 
	 * @param propertyName
	 * @param propertyInstance
	 */
	public <T extends PropertyInstance> void setPropertyValue(
			final String propertyName, final T propertyInstance) {

		@SuppressWarnings("unchecked")
		final Property<T> property = (Property<T>) getRenderArea()
				.getManagedDisplay().getDisplay().getDisplayAtoms()
				.getAtomByName(propertyName);

		getRenderArea().getPlatformRenderArea().setPropertyInstance(property,
				propertyInstance);
	}
}
