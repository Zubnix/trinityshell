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
package org.trinity.foundation.api.display.input;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/***************************************
 * A group of {@link InputModifier}s.
 ***************************************
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class InputModifiers {

	private final int inputModfiersState;

	/***************************************
	 * Construct a new <code>InputModifiers</code> instance with the logically
	 * or'ed masks of the desired {@link InputModifier}s.
	 *
	 * @param inputModifiersState
	 *            A logically or'ed integer.
	 ***************************************
	 */
	public InputModifiers(final int inputModifiersState) {
		this.inputModfiersState = inputModifiersState;
	}

	/***************************************
	 * Convenience function to check if the given {@link InputModifier} is
	 * active in this {@link InputModifiers} instance.
	 *
	 * @param modifier
	 *            The {@link InputModifier} to check
	 * @return True if in this group, false if not.
	 ***************************************
	 */
	public boolean isModifierSet(@Nonnull final InputModifier modifier) {
		return (getInputModifiersState() & modifier.getMask()) != 0;
	}

	/***************************************
	 * The logically or'ed masks of the grouped {@link InputModifier}s.
	 *
	 * @return A logically or'ed integer.
	 ***************************************
	 */
	public int getInputModifiersState() {
		return this.inputModfiersState;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof InputModifiers) {
			final InputModifiers otherInputModifiers = (InputModifiers) obj;
			return otherInputModifiers.getInputModifiersState() == getInputModifiersState();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.inputModfiersState;
	}
}
