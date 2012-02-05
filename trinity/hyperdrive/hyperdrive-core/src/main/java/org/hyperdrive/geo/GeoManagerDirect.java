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
package org.hyperdrive.geo;

import org.hydrogen.eventsystem.EventBus;

// TODO documentation
/**
 * A <code>GeoManagerDirect</code> directly executes geometry requests without
 * changes or manipulation. A <code>GeoManagerDirect</code> provides thus an
 * absolute layout.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class GeoManagerDirect extends EventBus implements GeoManager {

	@Override
	public void onResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateSizeValue();
	}

	@Override
	public void onMoveRequest(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdatePlaceValue();
	}

	@Override
	public void onMoveResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateSizePlaceValue();
	}

	@Override
	public void onLowerRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateLower();
	}

	@Override
	public void onRaiseRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateRaise();
	}

	@Override
	public void onChangeVisibilityRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateVisibility();
	}

	@Override
	public void onChangeParentRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateParentValue();
	}
}
