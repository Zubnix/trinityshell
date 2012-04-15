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
package org.hyperdrive.core;

import org.hydrogen.api.display.Area;
import org.hydrogen.api.display.AreaManipulator;
import org.hydrogen.api.geometry.Coordinates;
import org.hydrogen.display.EventPropagator;
import org.hydrogen.geometry.BaseCoordinates;
import org.hyperdrive.api.core.RenderArea;
import org.hyperdrive.api.geo.GeoExecutor;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.geo.GeoTransformation;

// TODO documentation
/**
 * A <code>RenderAreaGeoExecutor</code> is a delegate class for directly
 * manipulating a {@link RenderArea}'s geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
public class RenderAreaGeoExecutor extends AbstractGeoExecutor {
	private final RenderArea renderArea;

	/**
	 * 
	 * @param renderArea
	 */
	public RenderAreaGeoExecutor(final RenderArea renderArea) {
		this.renderArea = renderArea;
	}

	@Override
	public AreaManipulator<Area> getAreaManipulator() {
		return this.getAreaManipulator(getManipulatedArea());
	}

	@SuppressWarnings("unchecked")
	protected <T extends Area> AreaManipulator<T> getAreaManipulator(
			final RenderArea renderArea) {
		return (AreaManipulator<T>) renderArea.getPlatformRenderArea();
	}

	/**
	 * 
	 * @return
	 */
	public RenderArea getManipulatedArea() {
		return this.renderArea;
	}

	/**
	 * 
	 * @param renderArea
	 * @return
	 */
	protected boolean isAreaInitialized(final RenderArea renderArea) {
		if (renderArea == null) {
			return false;
		}
		final boolean initialized = (renderArea.getPlatformRenderArea() != null);
		return initialized;
	}

	@Override
	public void updatePlace(final int relativeX, final int relativeY) {
		if (isAreaInitialized(getManipulatedArea())) {
			final int newX = relativeX;
			final int newY = relativeY;
			final GeoTransformableRectangle currentParent = getManipulatedArea()
					.getParent();
			final Coordinates newRelativePosition = calculatePositionRelativeToTypedArea(
					currentParent, newX, newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();
			this.getAreaManipulator().move(newRelativeX, newRelativeY);
		}
	}

	@Override
	public void updateSize(final int width, final int height) {
		if (isAreaInitialized(getManipulatedArea())) {
			super.updateSize(width, height);
		}
	}

	@Override
	public void updateSizePlace(final int relativeX, final int relativeY,
			final int width, final int height) {
		final boolean areaInitialized = isAreaInitialized(getManipulatedArea());
		if (areaInitialized) {
			final int newX = relativeX;
			final int newY = relativeY;
			final int newWidth = width;
			final int newHeight = height;
			final GeoTransformableRectangle currentParent = getManipulatedArea()
					.getParent();

			final Coordinates newRelativePosition = calculatePositionRelativeToTypedArea(
					currentParent, newX, newY);

			final int newRelativeX = newRelativePosition.getX();
			final int newRelativeY = newRelativePosition.getY();

			final AreaManipulator<Area> areaManipulator = this
					.getAreaManipulator();
			areaManipulator.moveResize(newRelativeX, newRelativeY, newWidth,
					newHeight);
		}
	}

	@Override
	public void updateVisibility(final boolean visible) {
		if (isAreaInitialized(getManipulatedArea())) {
			super.updateVisibility(visible);
		}
	}

	protected Coordinates calculatePositionRelativeToTypedArea(
			final GeoTransformableRectangle directParent,
			final int directRelativeX, final int directRelativeY) {

		final RenderArea parentRenderArea = findClosestSameTypeArea(directParent);

		if (parentRenderArea == null) {
			return new BaseCoordinates(directRelativeX, directRelativeY);
		}

		final int newAbsX = directParent.getAbsoluteX() + directRelativeX;
		final int newAbsY = directParent.getAbsoluteY() + directRelativeY;

		final RenderArea root = getManipulatedArea().getManagedDisplay()
				.getRoot();
		final Coordinates absCorParent = getAreaManipulator(root)
				.translateCoordinates(getAreaPeer(parentRenderArea), 0, 0);

		final int newRelX = newAbsX - absCorParent.getX();
		final int newRelY = newAbsY - absCorParent.getY();

		final Coordinates corRelativeToTypedParent = new BaseCoordinates(
				newRelX, newRelY);

		return corRelativeToTypedParent;
	}

	/**
	 * 
	 * @param area
	 * 
	 */
	protected void initialize(final GeoTransformableRectangle parent,
			final GeoTransformableRectangle area) {
		initializeGeoTransformableSquare(parent, area);
		for (final GeoTransformableRectangle child : this.renderArea
				.getChildren()) {
			initialize(area, child);
		}
	}

	/**
	 * 
	 * @param area
	 * 
	 */
	protected void initializeGeoTransformableSquare(
			final GeoTransformableRectangle parent,
			final GeoTransformableRectangle area) {
		// Sublcasses should override & implement this method to make sure the
		// given area is ready to make back-end calls.
	}

	/**
	 * Perform any required operations on the parent before the manipulated
	 * render area is reparented.
	 * 
	 * @param newParentRenderArea
	 * 
	 */
	protected void preProcesNewSameTypeParent(
			final RenderArea newParentRenderArea) {
		newParentRenderArea.getPlatformRenderArea().propagateEvent(
				EventPropagator.REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES);
	}

	@Override
	public void updateParent(final GeoTransformableRectangle parent) {
		final RenderArea currentRenderArea = getManipulatedArea();
		final GeoTransformableRectangle newParent = parent;
		final RenderArea newParentRenderArea = findClosestSameTypeArea(newParent);

		final boolean newParentInitialized = isAreaInitialized(newParentRenderArea);
		final boolean currentRenderAreaInitialized = isAreaInitialized(currentRenderArea);

		if (newParentInitialized && !currentRenderAreaInitialized) {
			// parent is ready but we are not. we initialize ourself with
			// our ready parent as argument.
			initialize(newParent, currentRenderArea);
		} else if (newParentInitialized && currentRenderAreaInitialized) {
			// we are ready and our new parent is ready. we start the
			// procedure to change to our new ready parent.

			// call back-end to reparent current manipulated area
			final int newX = getManipulatedArea().getX();
			final int newY = getManipulatedArea().getY();

			if (currentRenderArea.equals(newParentRenderArea)) {
				throw new IllegalArgumentException(
						"Can not reparent to a child of self.");
			} else {
				final Coordinates newRelativePosition = calculatePositionRelativeToTypedArea(
						newParent, newX, newY);

				final int newRelativeX = newRelativePosition.getX();
				final int newRelativeY = newRelativePosition.getY();

				preProcesNewSameTypeParent(newParentRenderArea);
				reparent(getAreaPeer(newParentRenderArea), newRelativeX,
						newRelativeY);
			}
		} else if (currentRenderAreaInitialized && !newParentInitialized) {
			// we are ready but our new parent isn't. we hide ourself.

			// call back-end to hide current manipulated area
			this.getAreaManipulator().hide();
		} else {
			// If both the new parent and the current render area are
			// not initialized, there isn't much we can do. We don't want to
			// call any back-end.
		}
	}

	/**
	 * 
	 * @param renderArea
	 * @return
	 */
	protected Area getAreaPeer(final RenderArea renderArea) {
		return renderArea.getPlatformRenderArea();
	}

	/**
	 * Find the the closest parent in the area tree hierarchy that matches the
	 * type of {@link RenderAreaGeoExecutor#getManipulatedArea()}, starting from
	 * the given square.
	 * 
	 * @param square
	 *            The {@link GeoTransformableRectangle} to start searching from
	 *            upwards in the tree hierarchy.
	 * @return The closest parent with type {@link RenderArea}.
	 */
	protected RenderArea findClosestSameTypeArea(
			final GeoTransformableRectangle square) {

		// find the closest ancestor that is of type RenderArea
		if (square instanceof RenderArea) {

			return ((RenderArea) square);

		} else {
			final GeoTransformation transformation = square
					.toGeoTransformation();
			final GeoTransformableRectangle currentParent = transformation
					.getParent0();
			final GeoTransformableRectangle newParent = transformation
					.getParent1();
			if (currentParent == null) {
				if (newParent == null) {
					return null;
				} else {
					return findClosestSameTypeArea(newParent);
				}
			} else {
				return findClosestSameTypeArea(currentParent);
			}
		}
	}

	@Override
	public void destroy() {
		this.getAreaManipulator().destroy();
	}
}
