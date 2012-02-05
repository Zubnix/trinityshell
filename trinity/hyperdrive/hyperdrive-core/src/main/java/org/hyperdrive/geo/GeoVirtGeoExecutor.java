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

// TODO documentation
/**
 * A <code>GeoVirtGeoExecutor</code> executes the actual geometry changes for a
 * {@link GeoVirtRectangle}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GeoVirtGeoExecutor implements GeoExecutor {

	private final GeoVirtRectangle virtSquare;

	/**
	 * 
	 * @param virtSquare
	 *            The <code>GeoVirtRectangle</code> who's geometry will be
	 *            executed by this <code>GeoVirtGeoExecutor</code>.
	 */
	public GeoVirtGeoExecutor(final GeoVirtRectangle virtSquare) {
		this.virtSquare = virtSquare;
	}

	/**
	 * 
	 * @return
	 */
	public GeoVirtRectangle getVirtSquare() {
		return this.virtSquare;
	}

	@Override
	public void lower() {
		final GeoTransformableRectangle[] children = getVirtSquare()
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			child.requestLower();
		}
	}

	@Override
	public void raise() {
		final GeoTransformableRectangle[] children = getVirtSquare()
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			child.requestRaise();
		}
	}

	@Override
	public void updatePlace(final int relativeX, final int relativeY) {

		final GeoTransformation geoTransformation = getVirtSquare()
				.toGeoTransformation();

		final int deltaX = geoTransformation.getDeltaX();
		final int deltaY = geoTransformation.getDeltaY();

		final GeoTransformableRectangle[] children = getVirtSquare()
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			final int oldRelX = child.getRelativeX();
			final int oldRelY = child.getRelativeY();

			final int newRelX = oldRelX + deltaX;
			final int newRelY = oldRelY + deltaY;

			// directly manipulated underlying platform specific geometry of the
			// child
			child.getGeoExecutor().updatePlace(newRelX, newRelY);
		}
	}

	@Override
	public void updateSize(final int width, final int height) {
		// do nothing
	}

	@Override
	public void updateSizePlace(final int relativeX, final int relativeY,
			final int width, final int height) {
		updatePlace(relativeX, relativeY);
	}

	@Override
	public void updateVisibility(final boolean visible) {

		final GeoTransformableRectangle[] children = getVirtSquare()
				.getChildren();
		for (final GeoTransformableRectangle child : children) {
			updateChildVisibility(child);
		}
	}

	/**
	 * 
	 * @param child
	 * 
	 */
	private void updateChildVisibility(final GeoTransformableRectangle child) {
		final boolean childVisible = child.isVisible();
		// directly update underlying platform specific visibility of
		// the child
		child.getGeoExecutor().updateVisibility(childVisible);
	}

	@Override
	public void updateParent(final GeoTransformableRectangle parent) {
		final GeoTransformableRectangle[] children = getVirtSquare()
				.getChildren();

		for (final GeoTransformableRectangle child : children) {
			// directly update underlying platform specific parent of
			// the child
			child.getGeoExecutor().updateParent(getVirtSquare());

			updateChildVisibility(child);
		}
	}

	@Override
	public void destroy() {
		// TODO destroy all children. (Will somebody *please* think of the
		// children! D: )
	}
}
