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
package org.trinity.foundation.api.render.binding.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/****************************************
 * Marks a method as a manipulator of a property. After the execution of a
 * marked method, the properties with a matching name will be used to update the
 * bound view.
 * <p>
 * This annotation is for optional use with aspect oriented programming.
 * 
 * @see {@link org.trinity.foundation.api.render.binding.Binder#updateBinding(Object, String)}
 * 
 *************************************** 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface PropertyChanged {
	/****************************************
	 * The name(s) of the properties that were updated.
	 * 
	 * @return
	 *************************************** 
	 */
	String[] value();
}
