/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.scene;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.foundation.api.shared.Size;

/****************************************
 * An abstract base implementation of a {@link ShellNodeGeometryDelegate} for
 * use with an {@link AbstractShellNode}.
 *
 ***************************************
 */
@NotThreadSafe
public abstract class AbstractShellNodeGeometryDelegate implements ShellNodeGeometryDelegate {

	/**
	 * The geometry manipulator of node returned by {@link #getShellNode()}.
	 *
	 * @return a {@link DisplayAreaManipulator}
	 */
	public abstract DisplayAreaManipulator getShellNodeManipulator();

	@Override
	public void lower() {
		getShellNodeManipulator().lower();
	}

	@Override
	public void raise() {
		getShellNodeManipulator().raise();
	}

	@Override
	public void resize(@Nonnull final Size size) {
		getShellNodeManipulator().resize(	size.getWidth(),
											size.getHeight());
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
