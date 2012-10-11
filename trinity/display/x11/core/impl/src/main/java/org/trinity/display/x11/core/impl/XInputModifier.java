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
package org.trinity.display.x11.core.impl;

import org.trinity.foundation.input.api.InputModifier;

public class XInputModifier implements InputModifier {

	private final String name;
	private final int mask;

	public XInputModifier(final int mask, final String name) {
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
