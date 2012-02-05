/*
 * This file is part of Fusion-qtjambi.
 * 
 * Fusion-qtjambi is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Fusion-qtjambi is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-qtjambi. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.qt.error;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class RenderEngineTimeOutError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2151294354321403135L;

	private static final String MESSAGE = "Render engine did not start in %d seconds. Timed out.";

	/**
	 * 
	 * @param time
	 * @param timeUnit
	 */
	public RenderEngineTimeOutError(long time, TimeUnit timeUnit) {
		super(String.format(MESSAGE, timeUnit.toSeconds(time)));
	}
}