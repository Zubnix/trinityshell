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
package org.hydrogen.api.display.event;

/**
 * A <code>ConfigureRequestEvent</code> is a request from a display resource to
 * handle and set it's configuration as provided in the request.
 * <p>
 * A <code>ConfigureRequestEvent</code> holds all the information needed to
 * perform the configuration of the <code>EventSource</code> that emitted the
 * <code>DisplayEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ConfigureRequestEvent extends DisplayEvent {

	/**
	 * Indicates if the height of the <code>PlatformRenderArea</code> should be
	 * configured.
	 * 
	 * @return True if the height should be configured, false if not.
	 */
	boolean isHeightSet();

	/**
	 * The desired height of the <code>PlatformRenderArea</code> that emitted
	 * this <code>DisplayEvent</code>.
	 * 
	 * @return the desired height.
	 */
	int getHeight();

	/**
	 * The desired width of the <code>PlatformRenderArea</code> that emitted
	 * this <code>DisplayEvent</code>.
	 * 
	 * @return the desired width.
	 */
	int getWidth();

	/**
	 * The desired X coordinate of the <code>PlatformRenderArea</code> that
	 * emitted this <code>DisplayEvent</code>.
	 * 
	 * @return the desired X coordinate.
	 */
	int getX();

	/**
	 * The desired Y coordinate of the <code>PlatformRenderArea</code> that
	 * emitted this <code>DisplayEvent</code>.
	 * 
	 * @return the desired Y coordinate.
	 */
	int getY();

	/**
	 * Indicates if the width of the <code>PlatformRenderArea</code> should be
	 * configured.
	 * 
	 * @return
	 */
	boolean isWidthSet();

	/**
	 * Indicates if the X coordinate of the <code>PlatformRenderArea</code>
	 * should be configured.
	 * 
	 * @return True if the X coordinate should be configured, false if not.
	 */
	boolean isXSet();

	/**
	 * Indicates if the Y coordinate of the <code>PlatformRenderArea</code>
	 * should be configured.
	 * 
	 * @return True if the Y coordinate should be configured, false if not.
	 */
	boolean isYSet();
}
