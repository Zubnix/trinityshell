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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***************************************
 * Bind the annotated view element to a property. The property path is
 * constructed by 1 or more properties, seperated by a dot eg. "foo.bar.baz",
 * which would translate to getFoo().getBar().getBaz(). The property is resolved
 * relative to the parent model, that is, the data context of the enclosing view
 * element.
 * <p>
 * When this annotation is placed on class level, then all instances of that
 * class are considered as having this annotation on field level. When this
 * annotation is placed on both class and field level, the field level
 * annotation will have precedence.
 *************************************** 
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataContext {
	/***************************************
	 * A property path consisting of 1 or more property names seperated by a dot
	 * eg. "foo.bar.baz".
	 * 
	 * @return A property path
	 *************************************** 
	 */
	String value();
}
