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
package org.trinity.foundation.api.display.input;

import javax.annotation.concurrent.Immutable;

/***************************************
 * User input that came from a text input device.
 * 
 *************************************** 
 */
@Immutable
public class KeyboardInput extends Input {

	private final Key key;

	/***************************************
	 * Create a new <code>KeyboardInput</code> with the {@link Key} who's
	 * {@link Momentum} has changed.
	 * 
	 * @param momentum
	 *            a {@link Momentum}
	 * @param key
	 *            a {@link Key}
	 * @param inputModifiers
	 *            The {@link InputModifiers} that were active during the
	 *            creation of this <code>KeyboardInput</code>.
	 *************************************** 
	 */
	public KeyboardInput(	final Momentum momentum,
							final Key key,
							final InputModifiers inputModifiers) {
		super(	momentum,
				inputModifiers);
		this.key = key;
	}

	/***************************************
	 * the {@link Key} who's {@link Momentum} has changed.
	 * 
	 * @return a {@link Key}
	 *************************************** 
	 */
	public Key getKey() {
		return this.key;
	}
}
