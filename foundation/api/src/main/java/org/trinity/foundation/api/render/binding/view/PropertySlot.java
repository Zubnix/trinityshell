/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.api.render.binding.view;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***************************************
 * Marks a view method as the handler method for a changed model property. A
 * model property is linked to a view slot by matching the model property name
 * to the {@link #propertyName()}.
 * <p>
 * A {@code PropertySlot} is used as an argument of {@link PropertySlots}.
 * <p>
 * A property slot is invoked when a view is bound to its data context for the
 * first time, or when the property value changes.
 *
 ***************************************
 */
@Retention(RUNTIME)
@Target({})
public @interface PropertySlot {

	/**
	 * The datacontext for this specific annotation, relative to current active
	 * datacontext, either implicit (inherited) or explicit from a
	 * {@link DataModelContext}.
	 *
	 * @return A property path consisting of 1 or more property names seperated
	 *         by a dot eg. "foo.bar.baz".
	 */
	String dataModelContext() default "";

	/***************************************
	 * The model property name.
	 *
	 * @return a model property name.
	 ***************************************
	 */
	String propertyName();

	/***************************************
	 * The name of the view method that should be invoked.
	 *
	 * @return a view method name.
	 ***************************************
	 */
	String methodName();

	/***************************************
	 * The argument types of the view method that should be invoked.
	 *
	 * @return the argument types, in order.
	 ***************************************
	 */
	Class<?>[] argumentTypes();

	/***************************************
	 * An optional {@link PropertyAdapter} that will be used to transform the
	 * model property value to match the arguments of the method view.
	 *
	 * @return a {@link PropertyAdapter}.
	 ***************************************
	 */
	Class<? extends PropertyAdapter<?>> adapter() default DefaultPropertyAdapter.class;
}
