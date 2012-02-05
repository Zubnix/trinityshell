/*
 * This file is part of Hypercube.
 * 
 * Hypercube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hypercube is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hypercube. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hypercube;

import org.hydrogen.config.DisplayConfiguration;
import org.hyperdrive.core.ManagedDisplay;

/**
 * A <code>LogicLoadable</code> defines a number of methods that a behavioral
 * script must implement if it wants to be loadable by a
 * <code>LogicLoader</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface LogicLoadable {

	/**
	 * This method is called after the the back-end is initialized. It passes
	 * the initialized <code>ManagedDisplay</code> so it can be implemented for
	 * further managing.
	 * 
	 * @param managedDisplay
	 *            A {@link ManagedDisplay}.
	 */
	void postInit(ManagedDisplay managedDisplay);

	/**
	 * 
	 * @param displayConfiguration
	 */
	void preInit(DisplayConfiguration displayConfiguration);
}
