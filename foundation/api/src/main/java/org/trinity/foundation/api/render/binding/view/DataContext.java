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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/***************************************
 * Makes the annotated view element retrieve its model data relative to the
 * given data context. The data path is constructed by 1 or more getters,
 * separated by a dot eg. "foo.bar.baz", which would translate to
 * getFoo().getBar().getBaz(). The data context is resolved relative to the
 * 'parent' data context, that is, the data context of the enclosing view
 * element.
 * <p>
 * When this annotation is placed on class level, then all instances of that
 * class are considered as having this annotation. When this
 * annotation is placed on both class and field level, the field level
 * annotation will have precedence.
 ***************************************
 */
@Target({FIELD,
         TYPE})
@Retention(RUNTIME)
public @interface DataContext {
	/***************************************
	 * A path of getters consisting of 1 or more getter names seperated by a dot
	 * eg. "foo.bar.baz".
	 *
	 * @return A getters path
	 ***************************************
	 */
	String value();
}
