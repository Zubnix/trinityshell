/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.widget;

import org.trinity.foundation.display.api.DisplayAreaManipulator;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurface;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link BaseShellWidget}'s geometry. A
 * <code>BaseShellWidget</code> will ask it's <code>WidgetGeoExecutor</code> to
 * directly perform the requested geometric change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see ShellNodeExecutor
 */
public class BaseShellWidgetExecutor extends AbstractShellSurfaceExecutor {

	private final BaseShellWidget shellWidget;

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
		if ((square == null)) {
			return null;
		}
		if (square instanceof BaseShellWidget) {
			return (BaseShellWidget) square;
		}

		return findClosestSameTypeSurface(square.getParent());
	}

	@Override
	public PaintableSurfaceNode getSurfacePeer() {
		return getShellNode();
	}

	@Override
	protected PaintableSurfaceNode getSurfacePeer(final ShellSurface shellSurface) {
		// if the cast fails then somebody is trying to combine a non
		// PaintableSurfaceNode with one that is which shouldn'be a allowed.
		return (PaintableSurfaceNode) shellSurface;
	}

	@Override
	protected void initializeShellSurface(final ShellNodeParent parent) {
		// initialize the area with the closest typed parent.

		final BaseShellWidget shellWidgetImpl = getShellNode();
		final BaseShellWidget closestParentWidget = findClosestSameTypeSurface(parent);
		shellWidgetImpl.init(closestParentWidget);
	}
}
