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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***************************************
 * Marks a view method as the handler method for a changed property. A property
 * is linked to a slot by matching it's {@link ViewProperty#value()} to one the
 * {@link #value()}s of the slot.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface PropertySlot {
	String propertyName();

	String methodName();

	Class<?>[] argumentTypes();

	Class<? extends PropertyAdapter<?>> adapter() default DefaultPropertyAdapter.class;
}
