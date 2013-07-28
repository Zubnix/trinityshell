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
package org.trinity.foundation.display.x11.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.input.InputModifier;
import org.trinity.foundation.api.shared.ExecutionContext;

@ExecutionContext(DisplayExecutor.class)
@Immutable
public class XInputModifier implements InputModifier {

	private final String name;
	private final int mask;

	XInputModifier(	final int mask,
                       @Nonnull final String name) {
		this.name = name;
		this.mask = mask;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XInputModifier) {
			final XInputModifier otherXInputModifier = (XInputModifier) obj;
			return otherXInputModifier.getMask() == getMask();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.mask;
	}

	@Override
	public int getMask() {
		return this.mask;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
