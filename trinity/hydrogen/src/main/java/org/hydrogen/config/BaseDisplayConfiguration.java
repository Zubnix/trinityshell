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
package org.hydrogen.config;

import java.util.HashMap;
import java.util.Map;

import org.hydrogen.displayinterface.DisplayPlatform;

/**
 * A <code>BaseDisplayConfiguration</code> is a
 * <code>DisplayConfiguration</code> that allows it's configuration values to be
 * changed after it is created. This allows the programmer to alter the
 * <code>DisplayConfiguration</code> at runtime, before any {@link Display}
 * initialization takes place.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class BaseDisplayConfiguration implements DisplayConfiguration {

	private Map<String, String> backEndProperties;

	/**
	 * Create a new <code>BaseDisplayConfiguration</code> without any
	 * properties.
	 */
	public BaseDisplayConfiguration() {
		setBackEndProperties(new HashMap<String, String>());
	}

	@Override
	public Map<String, String> getBackEndProperties() {
		return this.backEndProperties;
	}

	/**
	 * Set the given backEndProperties. Any previously set properties will be
	 * overwritten.
	 * 
	 * @param backEndProperties
	 *            The backEndproperties that will be active on this
	 *            DisplayConfiguration.
	 */
	public void setBackEndProperties(final Map<String, String> backEndProperties) {
		this.backEndProperties = backEndProperties;
	}

	@Override
	public abstract DisplayPlatform initNewDisplayPlatform();
}
