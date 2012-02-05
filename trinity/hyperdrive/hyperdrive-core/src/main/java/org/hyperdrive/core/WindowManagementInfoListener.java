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

import org.hyperdrive.widget.VirtualRoot;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface WindowManagementInfoListener {
	/**
	 * 
	 * @param virtualRoot
	 */
	void onVirtualRootNew(final VirtualRoot virtualRoot);

	/**
	 * 
	 * @param virtualRoot
	 */
	void onVirtualRootDestroyed(final VirtualRoot virtualRoot);

	/**
	 * 
	 * @param virtualRootActive
	 */
	void onVirtualRootActivate(final VirtualRoot virtualRootActive);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowGainFocus(final ClientWindow clientWindow);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowLostFocus(final ClientWindow clientWindow);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowRaise(final ClientWindow clientWindow);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowLower(final ClientWindow clientWindow);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowNew(final ClientWindow clientWindow);

	/**
	 * 
	 * @param clientWindow
	 */
	void onClientWindowDestroyed(final ClientWindow clientWindow);
}
