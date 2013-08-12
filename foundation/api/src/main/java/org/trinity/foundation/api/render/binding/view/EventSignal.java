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
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.render.binding.view.delegate.Signal;

/***************************************
 * Mark a view as a producer of listenable events. It's up to the
 * {@link EventSignalFilter} to make sure the correct event listeners are
 * installed and the {@link Signal} is fired when an event arrives.
 * <p>
 * An {@link EventSignal} is used as an argument of {@link EventSignals}.
 *
 * @see Signal
 ***************************************
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface EventSignal {

	/***************************************
	 * The name of the no-args model method that should be invoked when an event
	 * arrives.
	 *
	 * @return a method name.
	 ***************************************
	 */
	String name();

	/**
	 * The type {@link EventSignalFilter} that will be used to listen for view
	 * events.
	 *
	 * @return
	 */
	Class<? extends EventSignalFilter> filter();

}
