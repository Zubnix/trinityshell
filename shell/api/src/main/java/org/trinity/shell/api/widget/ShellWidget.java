/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.widget;

import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.surface.ShellSurfaceParent;

/***************************************
 * Provides visual elements for the user to interact with in the shell scene.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface ShellWidget extends ShellSurfaceParent {

	/***************************************
	 * The object to interface with the render back-end. A {@code Painter}
	 * instance is dedicated to a single {@code ShellWidget} instance and should
	 * not change.
	 *
	 * @return a {@link Painter}.
	 ***************************************
	 */
	Painter getPainter();
}
