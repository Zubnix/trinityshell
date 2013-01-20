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
 * Marks a view method as the handler method for a changed model property. A
 * property is linked to a slot by matching the model property name to
 * {@link #propertyName()}.
 * <p>
 * A {@code PropertySlot} is used as an argument of {@link PropertySlots}.
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface PropertySlot {

	/**
	 * The datacontext of this specific annotation, relative to current active
	 * datacontext.
	 * 
	 * @return
	 */
	String dataContext() default "";

	/***************************************
	 * The model property name.
	 * 
	 * @return a model property name.
	 *************************************** 
	 */
	String propertyName();

	/***************************************
	 * The name of the view method that should be invoked when the model
	 * property changes.
	 * 
	 * @return a view method name.
	 *************************************** 
	 */
	String methodName();

	/***************************************
	 * The argument types of the view method that should be invoked when the
	 * model property changes.
	 * 
	 * @return the argument types.
	 *************************************** 
	 */
	Class<?>[] argumentTypes();

	/***************************************
	 * An optional {@link PropertyAdapter} that will be used to transform the
	 * model property to match the arguments of the method view.
	 * 
	 * @return a {@link PropertyAdapter}.
	 *************************************** 
	 */
	Class<? extends PropertyAdapter<?>> adapter() default DefaultPropertyAdapter.class;
}
