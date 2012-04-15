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

import org.hyperdrive.api.geo.GeoManager;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.geo.GeoTransformation;

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
public class GeoManagerDirect implements GeoManager {

	@Override
	public void onResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateSize();
	}

	@Override
	public void onMoveRequest(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdatePlace();
	}

	@Override
	public void onMoveResizeRequest(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		geoTransformable.doUpdateSizePlace();
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
		geoTransformable.doUpdateParent();
	}

	@Override
	public void onResizeNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onMoveNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onMoveResizeNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onLowerNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onRaiseNotify(final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onChangeVisibilityNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onChangeParentNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothing
	}

	@Override
	public void onDestroyNotify(
			final GeoTransformableRectangle geoTransformable,
			final GeoTransformation transformation) {
		// do nothin.

	}
}
