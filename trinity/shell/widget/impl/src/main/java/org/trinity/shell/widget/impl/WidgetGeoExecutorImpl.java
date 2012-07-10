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

import org.trinity.foundation.display.api.Area;
import org.trinity.foundation.display.api.AreaManipulator;
import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.shared.geometry.api.GeometryFactory;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.impl.RenderAreaGeoExecutorImpl;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.widget.api.Root;
import org.trinity.shell.widget.api.Widget;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link WidgetImpl}'s geometry. A <code>Widget</code> will ask
 * it's <code>WidgetGeoExecutor</code> to directly perform the requested
 * geometric change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
@Bind
@Named("Widget")
public class WidgetGeoExecutorImpl extends RenderAreaGeoExecutorImpl {

	/*****************************************
	 * @param root
	 * @param geometryFactory
	 ****************************************/
	@Inject
	protected WidgetGeoExecutorImpl(final Root root,
									final GeometryFactory geometryFactory) {
		super(root, geometryFactory);
	}

	@Override
	public AreaManipulator<Area> getAreaManipulator(final GeoTransformableRectangle geoTransformableRectangle) {
		return getAreaManipulator(geoTransformableRectangle);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Area> AreaManipulator<T> getAreaManipulator(final RenderArea renderArea) {
		AreaManipulator<T> manip = null;
		manip = (AreaManipulator<T>) ((Widget) renderArea).getPainter();
		return manip;
	}

	@Override
	protected Widget findClosestSameTypeArea(final GeoTransformableRectangle square) {
		if (square == null) {
			return null;
		}
		if (square instanceof WidgetImpl) {
			return (Widget) square;
		} else {
			return findClosestSameTypeArea(square.getParent());
		}
	}

	@Override
	protected Area getAreaPeer(final RenderArea renderArea) {
		return renderArea;
	}

	@Override
	protected void initializeGeoTransformableSquare(final GeoTransformableRectangle parent,
													final GeoTransformableRectangle area) {
		// initialize the area with the closest typed parent.
		if (area instanceof WidgetImpl) {
			final WidgetImpl widgetImpl = (WidgetImpl) area;
			final Widget closestParentWidget = findClosestSameTypeArea(parent);
			widgetImpl.init(closestParentWidget);
		}
	}

	@Override
	protected void preProcesNewSameTypeParent(final RenderArea newParentRenderArea) {
		// we don't want any processing done on our new same type parent.
	}

	@Override
	public void updateParent(	final GeoTransformableRectangle geoTransformableRectangle,
								final GeoTransformableRectangle parent) {
		final WidgetImpl widgetImpl = (WidgetImpl) geoTransformableRectangle;
		// reparent the widget
		super.updateParent(geoTransformableRectangle, parent);

		// If the old parent is null, we don't need to update the platform
		// render area.
		if (geoTransformableRectangle.toGeoTransformation().getParent0() != null) {
			final DisplayRenderArea parentPlatformRenderArea = widgetImpl
					.getParentPaintable().getPlatformRenderArea();
			final DisplayRenderArea platformRenderArea = widgetImpl
					.getPlatformRenderArea();

			if (parentPlatformRenderArea.equals(platformRenderArea)) {
				// we need to update the widget's platform render area
				final DisplayRenderArea newParentPlatformRenderArea = widgetImpl
						.getParentPaintable().getPlatformRenderArea();
				widgetImpl.setPlatformRenderArea(newParentPlatformRenderArea);
			}
		}
	}
}
