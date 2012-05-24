/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.xcb.error;

import org.trinity.core.input.api.InputModifiers;
import org.trinity.core.input.api.Key;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class UnknownKeyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -797047967274852800L;

	private static final String MESSAGE = "Got an unknown key name. Key code: %d, input modifiers mask %s";

	/**
	 * 
	 * @param baseKey
	 * @param baseInputModifiers
	 */
	public UnknownKeyException(final Key baseKey,
			final InputModifiers baseInputModifiers) {
		super(String.format(UnknownKeyException.MESSAGE, baseKey.getKeyCode(),
				Integer.toBinaryString(baseInputModifiers
						.getInputModifiersMask())));
	}
}
