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
package org.hydrogen.display.api;

// TODO documentation
/**
 * A <code>DisplayPlatform</code> groups methods for creating, accessing and
 * managing {@link Display} instances.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface DisplayPlatform {

	// TODO move to display and remove display argument?
	/**
	 * Register a <code>PlatformRenderArea</code> with the given resource handle
	 * for the given <code>Display</code>.
	 * <p>
	 * A resource handle is not chosen arbitrarily but rather provided by the
	 * underlying native display from the given <code>Display</code> parameter.
	 * This method is thus mainly used to register a
	 * <code>PlatformRenderArea</code> that was created on a known
	 * <code>Display</code> by a separate toolkit.
	 * 
	 * @param display
	 *            The {@link Display} where the newly registered
	 *            <code>PlatformRenderArea</code> belongs to.
	 * @param resourceHandle
	 *            The unique identifier for the <code>PlatformRenderArea</code>.
	 * @return
	 */
	PlatformRenderArea findPaintablePlatformRenderAreaFromId(Display display,
			ResourceHandle resourceHandle);

}
