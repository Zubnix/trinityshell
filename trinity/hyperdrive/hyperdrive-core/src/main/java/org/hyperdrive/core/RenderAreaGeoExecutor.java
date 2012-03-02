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

import org.hydrogen.displayinterface.Area;
import org.hydrogen.displayinterface.AreaManipulator;
import org.hydrogen.displayinterface.EventPropagator;
import org.hyperdrive.geo.GeoExecutor;
import org.hyperdrive.geo.GeoTransformableRectangle;
import org.hyperdrive.geo.GeoTransformation;

// TODO documentation
/**
 * A <code>RenderAreaGeoExecutor</code> is a delegate class for directly
 * manipulating an {@link AbstractRenderArea} subclass' geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
public class RenderAreaGeoExecutor extends AbstractGeoExecutor {
	private final AbstractRenderArea abstractRenderArea;

	/**
	 * 
	 * @param abstractRenderArea
	 */
	public RenderAreaGeoExecutor(final AbstractRenderArea abstractRenderArea) {
		this.abstractRenderArea = abstractRenderArea;
	}

	@Override
	public AreaManipulator<Area> getAreaManipulator() {
		return this.getAreaManipulator(getManipulatedArea());
	}

	@SuppressWarnings("unchecked")
	protected <T extends Area> AreaManipulator<T> getAreaManipulator(
			final AbstractRenderArea abstractRenderArea) {
		return (AreaManipulator<T>) abstractRenderArea.getPlatformRenderArea();
	}

	/**
	 * 
	 * @return
	 */
	public AbstractRenderArea getManipulatedArea() {
		return this.abstractRenderArea;
	}

	/**
	 * 
	 * @param abstractRenderArea
	 * @return
	 */
	protected boolean isAreaInitialized(
			final AbstractRenderArea abstractRenderArea) {
		if (abstractRenderArea == null) {
			return false;
		}
		final boolean initialized = (abstractRenderArea.getPlatformRenderArea() != null);
		return initialized;
	}

	@Override
	public void updatePlace(final int relativeX, final int relativeY) {
		if (isAreaInitialized(getManipulatedArea())) {
			final int newX = relativeX;
			final int newY = relativeY;
			final GeoTransformableRectangle currentParent = getManipulatedArea()
					.toGeoTransformation().getParent0();
			final int newRelativeX = calculateXRelativeToTypedArea(
					currentParent, newX);
			final int newRelativeY = calculateYRelativeToTypedArea(
					currentParent, newY);
			this.getAreaManipulator().move(newRelativeX, newRelativeY);
		}
		updateChildrenPosition();
	}

	/**
	 * 
	 * 
	 */
	protected void updateChildrenPosition() {
		for (final GeoTransformableRectangle child : getManipulatedArea()
				.getChildren()) {
			child.getGeoExecutor().updatePlace(child.getRelativeX(),
					child.getRelativeY());
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
					.toGeoTransformation().getParent0();
			final int newRelativeX = calculateXRelativeToTypedArea(
					currentParent, newX);
			final int newRelativeY = calculateYRelativeToTypedArea(
					currentParent, newY);
			final AreaManipulator<Area> areaManipulator = this
					.getAreaManipulator();
			areaManipulator.moveResize(newRelativeX, newRelativeY, newWidth,
					newHeight);
		}
		updateChildrenPosition();
	}

	@Override
	public void updateVisibility(final boolean visible) {
		if (isAreaInitialized(getManipulatedArea())) {
			super.updateVisibility(visible);
		}
	}

	/**
	 * 
	 * @param directParent
	 * @param directRelativeX
	 * @return
	 */
	private int calculateXRelativeToTypedArea(
			final GeoTransformableRectangle directParent,
			final int directRelativeX) {

		final AbstractRenderArea parentRenderArea = findRenderAreaWithUniqueAreaManipulator(findClosestSameTypeArea(directParent));

		if (parentRenderArea == null) {
			return directRelativeX;
		}
		final int newAbsX = directParent.getAbsoluteX() + directRelativeX;

		// FIXME we need to compensate for when the parentRenderArea has an
		// offset to it's actual platform renderarea. yRelativeToTypedArea
		// should be increased with this offset. A render area can have an
		// offset when it does not own (=match) it's platform render area.
		final int xRelativeToTypedArea = newAbsX
				- parentRenderArea.getAbsoluteX();

		return xRelativeToTypedArea;
	}

	/**
	 * 
	 * @param abstractRenderArea
	 * @return
	 */
	protected AbstractRenderArea findRenderAreaWithUniqueAreaManipulator(
			final AbstractRenderArea abstractRenderArea) {
		// we must find the topmost (in tree hierarchy) render area with an area
		// manipulator that is different of it's parent render area.

		final GeoTransformableRectangle parent = abstractRenderArea.getParent();

		// endless recursion safety
		if (parent == abstractRenderArea) {
			return abstractRenderArea;
		}

		final AbstractRenderArea parentRenderArea = findClosestSameTypeArea(parent);

		final AreaManipulator<? extends Area> areaManipulator = this
				.getAreaManipulator(abstractRenderArea);
		final AreaManipulator<? extends Area> parentAreaManipulator = this
				.getAreaManipulator(parentRenderArea);
		if (areaManipulator == parentAreaManipulator) {
			return findRenderAreaWithUniqueAreaManipulator(parentRenderArea);
		} else {
			return abstractRenderArea;
		}
	}

	/**
	 * 
	 * @param directParent
	 * @param directRelativeY
	 * @return
	 */
	private int calculateYRelativeToTypedArea(
			final GeoTransformableRectangle directParent,
			final int directRelativeY) {
		final AbstractRenderArea parentRenderArea = findRenderAreaWithUniqueAreaManipulator(findClosestSameTypeArea(directParent));
		if (parentRenderArea == null) {
			return directRelativeY;
		}
		final int newAbsY = directParent.getAbsoluteY() + directRelativeY;

		// FIXME we need to compensate for when the parentRenderArea has an
		// offset to it's actual platform renderarea. yRelativeToTypedArea
		// should be increased with this offset. A render area can have an
		// offset when it does not own (=match) it's platform render area.
		final int yRelativeToTypedArea = newAbsY
				- parentRenderArea.getAbsoluteY();

		return yRelativeToTypedArea;
	}

	/**
	 * 
	 * @param area
	 * 
	 */
	private void initialize(final GeoTransformableRectangle parent,
			final GeoTransformableRectangle area) {
		initializeGeoTransformableSquare(parent, area);
		for (final GeoTransformableRectangle child : this.abstractRenderArea
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
	 * 
	 * @param newParentRenderArea
	 * 
	 */
	protected void preProcesNewSameTypeParent(
			final AbstractRenderArea newParentRenderArea) {
		newParentRenderArea.getPlatformRenderArea().propagateEvent(
				EventPropagator.REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES);
	}

	@Override
	public void updateParent(final GeoTransformableRectangle parent) {
		final AbstractRenderArea currentRenderArea = getManipulatedArea();
		final GeoTransformableRectangle newParent = parent;
		final AbstractRenderArea newParentRenderArea = findClosestSameTypeArea(newParent);

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
			final int newX = getManipulatedArea().getRelativeX();
			final int newY = getManipulatedArea().getRelativeY();

			if (currentRenderArea.equals(newParentRenderArea)) {
				throw new IllegalArgumentException(
						"Can not reparent to a child of self.");
			} else {

				final int nativeNewRelX = calculateXRelativeToTypedArea(
						newParent, newX);
				final int nativeNewRelY = calculateYRelativeToTypedArea(
						newParent, newY);
				preProcesNewSameTypeParent(newParentRenderArea);
				reparent(getAreaPeer(newParentRenderArea), nativeNewRelX,
						nativeNewRelY);
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
	 * @param abstractRenderArea
	 * @return
	 */
	protected Area getAreaPeer(final AbstractRenderArea abstractRenderArea) {
		return abstractRenderArea.getPlatformRenderArea();
	}

	/**
	 * 
	 * @param square
	 * @return
	 */
	protected AbstractRenderArea findClosestSameTypeArea(
			final GeoTransformableRectangle square) {
		// FIXME this method must return a render area who's geometry matches
		// the native window it represents.

		// find the closest ancestor that is of type AbstractRenderArea
		if (square instanceof AbstractRenderArea) {

			return ((AbstractRenderArea) square);

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
