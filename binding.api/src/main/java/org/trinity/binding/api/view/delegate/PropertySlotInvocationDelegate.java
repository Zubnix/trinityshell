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
package org.trinity.binding.api.view.delegate;



import java.lang.reflect.Method;

/**
 *
 * Delegate to invoke a view method when the bound model property changes. This
 * delegate should be implemented for a specific widget toolkit.
 *
 * @see org.trinity.binding.api.view.PropertySlots
 * @see org.trinity.binding.api.view.PropertySlot
 *
 */
public interface PropertySlotInvocationDelegate {

    /**
     *
     * Invoke a view method with the given argument.
     *
     * @param view       The view who's method should be invoked.
     * @param viewMethod The method of the view to invoke.
     * @param argument   The argument to use when invoking the method. This can be an
     *                   array of objects, as defined in the var args of
     *                   {@link Method#invoke(Object, Object...)}.
     *
     */
    void invoke(Object view,
                Method viewMethod,
                Object argument);
}
