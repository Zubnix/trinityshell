/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.core;

import java.util.HashMap;
import java.util.Map;

import org.hydrogen.api.config.DisplayConfiguration;
import org.hyperdrive.api.core.ManagedDisplay;

/**
 * A <code>DisplayManager</code> gives access to the underlying native
 * display(s) by creating a <code>ManagedDisplay</code> for each native display
 * that needs managing.
 * <p>
 * A <code>DisplayManager</code> also keeps track of which
 * <code>ManagedDisplay</code>s are created by mapping each
 * <code>ManagedDisplay</code> to it's native display name. A
 * <code>DisplayManager</code> will only keep track of
 * <code>ManagedDisplay<code>s if was created by the <code>ManagedDispay</code>
 * itself.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class DisplayManager {
	private static volatile DisplayManager instance = new DisplayManager();
	private final Map<String, ManagedDisplay> managedDisplayMap;

	/**
	 * Gets the default <code>DisplayManager</code> instance.
	 * <p>
	 * The advantage of this default <code>DisplayManager</code> is that all
	 * managed displays can easily be tracked in a single central place.
	 * 
	 * @return A new <code>DisplayManager</code> when called for the first time.
	 *         Any succeeding call will return the originally created instance.
	 */
	public static synchronized DisplayManager instance() {
		if (DisplayManager.instance == null) {
			DisplayManager.instance = new DisplayManager();
		}
		return DisplayManager.instance;
	}

	/**
	 * Create a new <code>DisplayManager</code>. This new
	 * <code>DisplayManager</code> does not initially manage any native
	 * displays, unless this <code>DisplayManager</code> is explicitly told to
	 * do so with a call to <code>getNewManagedDisplay</code>.
	 */
	public DisplayManager() {
		this.managedDisplayMap = new HashMap<String, ManagedDisplay>(6);
	}

	/**
	 * Check the given <code>DisplayConfiguration</code> so that it's given
	 * display name is not already bound to a
	 * <code>ManagedDisplay<code>. If not, the display name is returned.
	 * 
	 * @param displayConfiguration
	 *            A {@link DisplayConfiguration}.
	 * @return The display name of the given <code>DisplayConfiguration</code>.
	 */
	private String checkDisplayConfiguration(
			final DisplayConfiguration displayConfiguration) {

		final String displayName = displayConfiguration.getBackEndProperties()
				.get(DisplayConfiguration.DISPLAY);

		if (this.managedDisplayMap.containsKey(displayName)) {
			throw new IllegalArgumentException(String.format(
					"DisplayAddress: %s already in use.", displayName));
		}
		return displayName;
	}

	/**
	 * Create a new <code>ManagedDisplay</code> that will manage the native
	 * display as defined by the given <code>DisplayConfiguration</code>.
	 * <p>
	 * The returned <code>ManagedDisplay</code> can be accessed again by calling
	 * <code>getManagedDisplay</code>.
	 * 
	 * @param displayConfiguration
	 *            A {@link DisplayConfiguration}.
	 * @return A new {@link ManagedDisplay}.
	 */
	public ManagedDisplay getNewManagedDisplay(
			final DisplayConfiguration displayConfiguration) {

		ManagedDisplay managedDisplay;
		final String displayName = checkDisplayConfiguration(displayConfiguration);
		managedDisplay = new BaseManagedDisplay(displayConfiguration
				.initNewDisplayPlatform().newDisplay(displayName));

		this.managedDisplayMap.put(displayName, managedDisplay);

		return managedDisplay;
	}
}
