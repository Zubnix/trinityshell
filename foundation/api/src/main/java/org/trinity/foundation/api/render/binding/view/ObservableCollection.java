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
 * Bind every child view of the marked view to the respective element of the
 * referenced collection. Correctly handling these child views is delegated to
 * the {@link org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate}.
 ***************************************
 */
@Retention(RUNTIME)
@Target({FIELD,
         TYPE})
public @interface ObservableCollection {

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
	 * The referenced collection property. Valid collection types are
	 * implementation dependent.
	 *
	 * @return a property name.
	 ***************************************
	 */
	String value();

	/***************************************
	 * The child view class to use when instantiating a new child view. This can
	 * be any view class that can be instantiated by Guice.
	 *
	 * @return a view type.
	 ***************************************
	 */
	Class<?> view();
}