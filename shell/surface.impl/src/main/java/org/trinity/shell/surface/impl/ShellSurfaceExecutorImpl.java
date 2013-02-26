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
package org.trinity.shell.surface.impl;

import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.AbstractShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurface;

public class ShellSurfaceExecutorImpl extends AbstractShellSurfaceExecutor {

	private final AbstractShellSurface abstractShellSurface;

	public ShellSurfaceExecutorImpl(final AbstractShellSurface shellSurface) {
		this.abstractShellSurface = shellSurface;
	}

	@Override
	protected DisplaySurface getSurfacePeer(final ShellSurface shellSurface) {
		return shellSurface.getDisplaySurface();
	}

	@Override
	protected ShellSurface findClosestSameTypeSurface(final ShellNode square) {
		if (square == null) {
			return null;
		}

		if (square instanceof ShellSurface) {

			return (ShellSurface) square;
		}

		final ShellNodeParent parent = square.getParent();
		if ((parent == null) || parent.equals(square)) {
			return null;
		}

		return findClosestSameTypeSurface(parent);
	}

	@Override
	public AbstractShellSurface getShellNode() {
		return this.abstractShellSurface;
	}

	@Override
	public DisplayAreaManipulator getShellNodeManipulator() {
		return getShellNode().getDisplaySurface();
	}
}