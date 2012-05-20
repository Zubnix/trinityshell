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
package org.hyperdrive.widget.impl;

import org.hydrogen.display.api.Area;
import org.hydrogen.display.api.AreaManipulator;
import org.hydrogen.display.api.PlatformRenderArea;
import org.hyperdrive.foundation.api.RenderArea;
import org.hyperdrive.foundation.impl.RenderAreaGeoExecutor;
import org.hyperdrive.geo.api.GeoExecutor;
import org.hyperdrive.geo.api.GeoTransformableRectangle;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link BaseWidget}'s geometry. A <code>Widget</code> will ask
 * it's <code>WidgetGeoExecutor</code> to directly perform the requested
 * geometric change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
public class BaseWidgetGeoExecutor extends RenderAreaGeoExecutor {

	/**
	 * 
	 * @param baseWidget
	 */
	public BaseWidgetGeoExecutor(final BaseWidget baseWidget) {
		super(baseWidget);
	}

	@Override
	public AreaManipulator<Area> getAreaManipulator() {
		return getAreaManipulator(getManipulatedArea());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Area> AreaManipulator<T> getAreaManipulator(
			final RenderArea renderArea) {
		AreaManipulator<T> manip = null;
		manip = (AreaManipulator<T>) ((BaseWidget) renderArea).getPainter();
		return manip;
	}

	@Override
	protected BaseWidget findClosestSameTypeArea(
			final GeoTransformableRectangle square) {
		if (square == null) {
			return null;
		}
		if (square instanceof BaseWidget) {
			return (BaseWidget) square;
		} else {
			return findClosestSameTypeArea(square.getParent());
		}
	}

	@Override
	protected Area getAreaPeer(final RenderArea renderArea) {
		return renderArea;
	}

	@Override
	public BaseWidget getManipulatedArea() {
		return (BaseWidget) super.getManipulatedArea();
	}

	@Override
	protected void initializeGeoTransformableSquare(
			final GeoTransformableRectangle parent,
			final GeoTransformableRectangle area) {
		// initialize the area with the closest typed parent.
		if (area instanceof BaseWidget) {
			final BaseWidget baseWidget = (BaseWidget) area;
			final BaseWidget closestParentWidget = findClosestSameTypeArea(parent);
			baseWidget.init(closestParentWidget);
		}
	}

	@Override
	protected void preProcesNewSameTypeParent(
			final RenderArea newParentRenderArea) {
		// we don't want any processing done on our new same type parent.
	}

	@Override
	public void updateParent(final GeoTransformableRectangle parent) {
		final BaseWidget baseWidget = getManipulatedArea();
		// reparent the widget
		super.updateParent(parent);

		// If the old parent is null, we don't need to update the platform
		// render area.
		if (getManipulatedArea().toGeoTransformation().getParent0() != null) {
			final PlatformRenderArea parentPlatformRenderArea = baseWidget
					.getParentPaintable().getPlatformRenderArea();
			final PlatformRenderArea platformRenderArea = baseWidget
					.getPlatformRenderArea();

			if (parentPlatformRenderArea.equals(platformRenderArea)) {
				// we need to update the widget's platform render area
				final PlatformRenderArea newParentPlatformRenderArea = baseWidget
						.getParentPaintable().getPlatformRenderArea();
				baseWidget.setPlatformRenderArea(newParentPlatformRenderArea);
			}
		}
	}
}
