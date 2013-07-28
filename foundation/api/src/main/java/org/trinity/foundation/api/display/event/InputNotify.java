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
package org.trinity.foundation.api.display.event;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.shared.ExecutionContext;

// TODO documentation
/**
 * Represents a general user input notification. This can be, for example, a
 * keyboard key that was pressed or a mouse button that was released.
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class InputNotify<I extends Input> extends DisplayEvent {

	private final I input;

	/***************************************
	 * Create a new <code>InputNotify</code> that targets a given display
	 * resource. The input detail is described by the given {@link Input}
	 * argument.
	 * @param input
	 *            an {@link Input}
	 ***************************************
	 */
	public InputNotify(@Nonnull final I input) {
		this.input = input;
	}

	/***************************************
	 * The object describing the user input.
	 *
	 * @return {@link Input}
	 ***************************************
	 */
	public I getInput() {
		return this.input;
	}
}
