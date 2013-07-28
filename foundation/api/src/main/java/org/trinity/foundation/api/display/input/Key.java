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

import javax.annotation.concurrent.Immutable;

/***************************************
 * Identifies a text input device's key, ie a keyboard key.
 ***************************************
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class Key {

	private final int keyCode;

	/***************************************
	 * Construct a new <code>Key</code> with the given native key code.
	 *
	 * @param keyCode
	 *            the native code of this </code>Key</code>
	 ***************************************
	 */
	public Key(final int keyCode) {
		this.keyCode = keyCode;
	}

	/***************************************
	 * The native code of this <code>Key</code>. This code uniquely identifies a
	 * <code>Key</code>.
	 *
	 * @return the native code of this </code>Key</code>
	 ***************************************
	 */
	public int getKeyCode() {
		return this.keyCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Key) {
			final Key otherKey = (Key) obj;
			return getKeyCode() == otherKey.getKeyCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getKeyCode();
	}
}
