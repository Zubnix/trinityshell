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
package org.trinity.core.config.api;

import java.util.Map;

import org.trinity.core.display.api.DisplayPlatform;

/**
 * A <code>DisplayConfiguration</code> declares options and properties that are
 * needed by for a <code>DisplayPlatform</code> implementation as well as the
 * <code>DisplayPlatform</code> itself.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface DisplayConfiguration {

	/**
	 * The propertyname that identifies the X server to connect to.
	 * <p>
	 * Valid values for this X11 property are: ":0", ":1" etc.
	 * <p>
	 * The default on X11 is the value of the environment variable: DISPLAY.
	 */
	static final String DISPLAY = "display";

	// /**
	// * Create a new {@link DisplayPlatform} configured by this
	// * <code>DisplayConfiguration</code>.
	// *
	// * @return
	// */
	// DisplayPlatform initNewDisplayPlatform();

	/**
	 * Retrieve all properties that were set on this
	 * <code>DisplayConfiguration</code>.
	 * <p>
	 * These properties can be used by newly created
	 * <code>DisplayPlatform</code>s to further initialize any back-ends.
	 * 
	 * @return The properties configured on this
	 *         <code>DisplayConfiguration</code>.
	 */
	Map<String, String> getBackEndProperties();

	/**
	 * Add a {@link Runnable} that will be executed before a
	 * {@link DisplayPlatform} is initialized. This can be used to do initialize
	 * resource that are needed before any <code>DisplayPlatform</code> is
	 * created.
	 * 
	 * @param configPerform
	 */
	void addConfigPerform(Runnable configPerform);

	Runnable[] getConfigPerforms();
}
