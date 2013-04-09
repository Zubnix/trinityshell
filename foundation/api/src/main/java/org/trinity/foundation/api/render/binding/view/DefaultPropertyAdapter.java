/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.shared.OwnerThread;

/***************************************
 * A default implementation of a {@link PropertyAdapter}. It simply returns the
 * property value without modification. Used as the default value in
 * {@link PropertySlot#adapter()}.
 * 
 *************************************** 
 */
@OwnerThread("Render")
public class DefaultPropertyAdapter implements PropertyAdapter<Object> {

	@Override
	public Object adapt(final Object property) {
		return property;
	}
}