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
package org.hyperdrive.widget;

import org.hydrogen.displayinterface.Area;
import org.hydrogen.displayinterface.AreaManipulator;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hyperdrive.core.AbstractRenderArea;
import org.hyperdrive.core.RenderAreaGeoExecutor;
import org.hyperdrive.geo.GeoTransformableRectangle;

// TODO documentation
/**
 * A <code>WidgetGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link Widget}'s geometry. A <code>Widget</code> will ask it's
 * <code>WidgetGeoExecutor</code> to directly perform the requested geometric
 * change.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
public class WidgetGeoExecutor extends RenderAreaGeoExecutor {

	/**
	 * 
	 * @param widget
	 */
	public WidgetGeoExecutor(final Widget widget) {
		super(widget);
	}

	@Override
	public AreaManipulator<Area> getAreaManipulator() {
		return getAreaManipulator(getManipulatedArea());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Area> AreaManipulator<T> getAreaManipulator(
			final AbstractRenderArea abstractRenderArea) {
		AreaManipulator<T> manip = null;
		manip = (AreaManipulator<T>) ((Widget) abstractRenderArea).getPainter();
		return manip;
	}

	@Override
	protected Widget findClosestSameTypeArea(
			final GeoTransformableRectangle square) {
		if (square == null) {
			return null;
		}
		if (square instanceof Widget) {
			return (Widget) square;
		} else {
			return findClosestSameTypeArea(square.getParent());
		}
	}

	@Override
	protected Area getAreaPeer(final AbstractRenderArea abstractRenderArea) {
		return abstractRenderArea;
	}

	@Override
	public Widget getManipulatedArea() {
		return (Widget) super.getManipulatedArea();
	}

	@Override
	protected void initializeGeoTransformableSquare(
			final GeoTransformableRectangle parent,
			final GeoTransformableRectangle area) {
		// initialize the area with the closest typed parent.
		if (area instanceof Widget) {
			final Widget widget = (Widget) area;
			final Widget closestParentWidget = findClosestSameTypeArea(parent);
			widget.init(closestParentWidget);
		}
	}

	@Override
	protected void preProcesNewSameTypeParent(
			final AbstractRenderArea newParentRenderArea) {
		// we don't want any processing done on our new same type parent.
	}

	@Override
	public void updateParent(final GeoTransformableRectangle parent) {
		final Widget widget = getManipulatedArea();
		// reparent the widget
		super.updateParent(parent);

		// If the old parent is null, we don't need to update the platform
		// render area.
		if (getManipulatedArea().toGeoTransformation().getParent0() != null) {
			final PlatformRenderArea parentPlatformRenderArea = widget
					.getParentPaintable().getPlatformRenderArea();
			final PlatformRenderArea platformRenderArea = widget
					.getPlatformRenderArea();

			if (parentPlatformRenderArea.equals(platformRenderArea)) {
				// we need to update the widget's platform render area
				final PlatformRenderArea newParentPlatformRenderArea = widget
						.getParentPaintable().getPlatformRenderArea();
				widget.setPlatformRenderArea(newParentPlatformRenderArea);
			}
		}
	}
}
