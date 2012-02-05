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
package org.hyperdrive.widget;

import org.hydrogen.paintinterface.PaintCall;

// TODO bind a painter to a view & automatically call the painter when a view's
// paint method is called (ie annotate said method)
// TODO documentation
/**
 * A <code>View</code> is the visual delegate for a {@link Widget}. A
 * <code>Widget</code> will call a <code>View</code>'s methods when it needs to
 * update it's visual representation.
 * <p>
 * Design consideration:
 * <p>
 * A <code>View</code> should always return {@link PaintCall} objects. These
 * objects must be passed to the <code>Widget</code>'s painter if any painting
 * needs to be done.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface View {

	/**
	 * Called by every <code>Widget</code> on initialization.
	 * 
	 * @param args
	 * @return
	 */
	PaintCall<?, ?> onCreate(Object... args);

	/**
	 * Called by every <code>Widget</code> on destruction.
	 * 
	 * @param args
	 * @return
	 */
	PaintCall<?, ?> onDestroy(Object... args);
}