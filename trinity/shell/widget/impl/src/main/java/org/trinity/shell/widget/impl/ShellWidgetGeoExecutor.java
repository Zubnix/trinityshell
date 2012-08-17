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
package org.trinity.shell.widget.impl;

import javax.inject.Named;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplayAreaManipulator;
import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.core.impl.ShellRenderAreaGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.widget.api.ShellWidget;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link ShellWidgetImpl}'s geometry. A <code>ShellWidget</code>
 * will ask it's <code>WidgetGeoExecutor</code> to directly perform the
 * requested geometric change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see ShellGeoExecutor
 */
@Bind
@Named("ShellWidget")
public class ShellWidgetGeoExecutor extends ShellRenderAreaGeoExecutor {

	/*****************************************
	 * @param shellRoot
	 * @param geometryFactory
	 ****************************************/
	@Inject
	protected ShellWidgetGeoExecutor(final ShellRoot shellRoot) {
		super(shellRoot);
	}

	@Override
	public DisplayAreaManipulator<DisplayArea> getAreaManipulator(final ShellGeoNode shellGeoNode) {
		return getAreaManipulator(shellGeoNode);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends DisplayArea> DisplayAreaManipulator<T> getAreaManipulator(final ShellRenderArea shellRenderArea) {
		DisplayAreaManipulator<T> manip = null;
		manip = (DisplayAreaManipulator<T>) ((ShellWidget) shellRenderArea)
				.getPainter();
		return manip;
	}

	@Override
	protected ShellWidget findClosestSameTypeArea(final ShellGeoNode square) {
		if (square == null) {
			return null;
		}
		if (square instanceof ShellWidgetImpl) {
			return (ShellWidget) square;
		} else {
			return findClosestSameTypeArea(square.getParent());
		}
	}

	@Override
	protected DisplayArea getAreaPeer(final ShellRenderArea shellRenderArea) {
		return shellRenderArea;
	}

	@Override
	protected void initializeGeoTransformableSquare(final ShellGeoNode parent,
													final ShellGeoNode area) {
		// initialize the area with the closest typed parent.
		if (area instanceof ShellWidgetImpl) {
			final ShellWidgetImpl shellWidgetImpl = (ShellWidgetImpl) area;
			final ShellWidget closestParentWidget = findClosestSameTypeArea(parent);
			shellWidgetImpl.init(closestParentWidget);
		}
	}

	@Override
	protected void preProcesNewSameTypeParent(final ShellRenderArea newParentRenderArea) {
		// we don't want any processing done on our new same type parent.
	}

	@Override
	public void reparent(	final ShellGeoNode shellGeoNode,
							final ShellGeoNode parent) {
		final ShellWidgetImpl shellWidgetImpl = (ShellWidgetImpl) shellGeoNode;
		// reparent the widget
		super.reparent(shellGeoNode, parent);

		// If the old parent is null, we don't need to update the platform
		// render area.
		if (shellGeoNode.toGeoTransformation().getParent0() != null) {
			final DisplayRenderArea parentPlatformRenderArea = shellWidgetImpl
					.getParentPaintable().getDisplayRenderArea();
			final DisplayRenderArea platformRenderArea = shellWidgetImpl
					.getDisplayRenderArea();

			if (parentPlatformRenderArea.equals(platformRenderArea)) {
				// we need to update the widget's platform render area
				final DisplayRenderArea newParentPlatformRenderArea = shellWidgetImpl
						.getParentPaintable().getDisplayRenderArea();
				shellWidgetImpl
						.setPlatformRenderArea(newParentPlatformRenderArea);
			}
		}
	}
}
