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

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.shell.api.scene.AbstractAsyncShellNode;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeExecutor;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurface;

// TODO documentation
/**
 * A {@link ShellNodeExecutor} for a {@link BaseShellWidget}.
 * 
 */
@NotThreadSafe
public class BaseShellWidgetExecutor extends AbstractShellSurfaceExecutor {

	private final BaseShellWidget shellWidget;

	/***************************************
	 * Create a new {@code BaseShellWidgetExecutor} for the given
	 * {@code BaseShellWidget}.
	 * 
	 * @param shellWidget
	 *            the {@link BaseShellWidget} to manage.
	 *************************************** 
	 */
	public BaseShellWidgetExecutor(final BaseShellWidget shellWidget) {
		this.shellWidget = shellWidget;
	}

	@Override
	public BaseShellWidget getShellNode() {
		return this.shellWidget;
	}

	@Override
	public DisplayAreaManipulator getShellNodeManipulator() {
		return getShellNode().getPainter();
	}

	@Override
	protected BaseShellWidget findClosestSameTypeSurface(final ShellNode square) {
		checkArgument(square instanceof AbstractAsyncShellNode);

		if (square == null) {
			return null;
		}
		// find the closest ancestor that is of type ShellSurface
		if (square instanceof BaseShellWidget) {

			return (BaseShellWidget) square;
		}

		final ShellNodeParent parent = ((AbstractAsyncShellNode) square).getParentImpl();
		if ((parent == null) || parent.equals(square)) {
			return null;
		}

		return findClosestSameTypeSurface(parent);
	}

	@Override
	protected BaseShellWidget getSurfacePeer(final ShellSurface shellSurface) {
		// if the cast fails then somebody is trying to combine a non
		// BaseShellWidget with one that is, which shouldn'be a allowed.
		return (BaseShellWidget) shellSurface;
	}
}
