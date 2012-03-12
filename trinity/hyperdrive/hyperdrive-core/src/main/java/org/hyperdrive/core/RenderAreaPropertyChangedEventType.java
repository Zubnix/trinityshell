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

import org.hydrogen.api.event.Type;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class RenderAreaPropertyChangedEventType implements Type {

	private final String propertyName;

	/**
	 * 
	 * @param propertyName
	 */
	protected RenderAreaPropertyChangedEventType(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return this.propertyName;
	}
}
