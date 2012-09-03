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

import javax.inject.Named;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.surface.AbstractShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link BaseShellWidget}'s geometry. A <code>BaseShellWidget</code>
 * will ask it's <code>WidgetGeoExecutor</code> to directly perform the
 * requested geometric change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see ShellNodeExecutor
 */
@Bind
@Named("shellWidgetGeoExecutor")
public class ShellWidgetGeoExecutor extends AbstractShellSurfaceExecutor {

	@Inject
	ShellWidgetGeoExecutor(@Named("ShellRootSurface") final ShellSurface shellRoot) {
		super(shellRoot);
	}

	@Override
	public DisplayAreaManipulator<DisplayArea> getShellNodeManipulator(final ShellNode shellNode) {
		return getAreaManipulator((ShellSurface) shellNode);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(final ShellSurface shellSurface) {
		final DisplayAreaManipulator<T> manip = (DisplayAreaManipulator<T>) ((BaseShellWidget) shellSurface).getPainter();
		return manip;
	}

	@Override
	protected BaseShellWidget findClosestSameTypeSurface(final ShellNode square) {
		if ((square == null) || (square.getParent() == square)) {
			return null;
		}
		if (square instanceof BaseShellWidget) {
			return (BaseShellWidget) square;
		}

		return findClosestSameTypeSurface(square.getParent());
	}

	@Override
	protected DisplayArea getAreaPeer(final ShellSurface shellSurface) {
		return shellSurface;
	}

	@Override
	protected void initializeShellSurface(	final ShellNode parent,
											final ShellNode shellNode) {
		// initialize the area with the closest typed parent.
		if (shellNode instanceof BaseShellWidget) {
			final BaseShellWidget shellWidgetImpl = (BaseShellWidget) shellNode;
			final BaseShellWidget closestParentWidget = findClosestSameTypeSurface(parent);
			shellWidgetImpl.init(closestParentWidget);
		}
	}
}
