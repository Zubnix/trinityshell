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
package org.trinity.shell.api.node;

import org.trinity.foundation.api.display.DisplayAreaManipulator;

/****************************************
 * An abstract base implemenation of a {@link ShellNodeExecutor} for use with an
 * {@link AbstractShellNode}.
 * 
 *************************************** 
 */
public abstract class AbstractShellNodeExecutor implements ShellNodeExecutor {

	public abstract DisplayAreaManipulator getShellNodeManipulator();

	@Override
	public abstract ShellNode getShellNode();

	@Override
	public void lower() {
		getShellNodeManipulator().lower();
	}

	@Override
	public void raise() {
		getShellNodeManipulator().raise();
	}

	@Override
	public void resize(	final int width,
						final int height) {
		getShellNodeManipulator().resize(	width,
											height);
	}

	@Override
	public void show() {
		getShellNodeManipulator().show();
	}

	@Override
	public void hide() {
		getShellNodeManipulator().hide();
	}

	@Override
	public void destroy() {
		getShellNodeManipulator().destroy();
	}
}
