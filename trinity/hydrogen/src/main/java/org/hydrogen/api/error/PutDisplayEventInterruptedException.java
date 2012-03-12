/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.api.error;

import org.hydrogen.api.display.event.DisplayEvent;

/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class PutDisplayEventInterruptedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2139011721150422619L;

	private static final String MESSAGE = "Interrupted while waiting for free space on the display event queue. The display event '%s' could not be added.";

	/**
	 * 
	 * @param e
	 * @param displayEvent
	 */
	public PutDisplayEventInterruptedException(final InterruptedException e,
			DisplayEvent displayEvent) {
		super(String.format(MESSAGE, displayEvent), e);
	}
}
