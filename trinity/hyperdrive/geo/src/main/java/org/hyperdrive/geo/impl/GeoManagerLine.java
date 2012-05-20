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
package org.hyperdrive.geo.impl;

import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.hyperdrive.geo.api.GeoTransformation;
import org.hyperdrive.geo.api.base.AbstractGeoManagerWithChildren;
import org.hyperdrive.geo.api.base.Margins;

// TODO documentation
// TODO evaluate layout algoritm corner cases (negative values that shouldn't
// be negative. childs with size 0, ...)
//TODO refactor to reuse code and for cleaner reading
/**
 * A <code>GeoManagerLine</code> provides a layout for children in a horizontal
 * or vertical line. All children will have the same height or width for a
 * horizontal or vertical layout respectively and are placed directly next to
 * each other with no overlap or spacing. The other child dimensions are
 * determined by the child's {@link LineProperty}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class GeoManagerLine extends
		AbstractGeoManagerWithChildren<LineProperty> {

	private boolean horizontalDirection;
	private boolean inverseDirection;

	/**
	 * Create a new <code>GeoManagerLine</code> that will use the geometry of
	 * the given container to determine the layout of the managed children.
	 * Ideally, the given container should be the same as the
	 * <code>GeoTransformableRectangle</code> that returns this
	 * <code>GeoManager</code>.
	 * 
	 * @param container
	 * @param horizontalDirection
	 * @param inverseDirection
	 */
	public GeoManagerLine(final GeoTransformableRectangle container,
			final boolean horizontalDirection, final boolean inverseDirection) {
		super(container);
		setHorizontalDirection(horizontalDirection);
		setInverseDirection(inverseDirection);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isHorizontalDirection() {
		return this.horizontalDirection;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInverseDirection() {
		return this.inverseDirection;
	}

	/**
	 * 
	 * @param horizontalDirection
	 */
	public void setHorizontalDirection(final boolean horizontalDirection) {
		this.horizontalDirection = horizontalDirection;
	}

	/**
	 * 
	 * @param inverseDirection
	 */
	public void setInverseDirection(final boolean inverseDirection) {
		this.inverseDirection = inverseDirection;
	}

	/**
	 * 
	 * @param square
	 */
	protected void cancelMoveResize(final GeoTransformableRectangle square) {
		// make sure the square doesn't move or resizes
		square.cancelPendingMove();
		square.cancelPendingResize();
	}

	@Override
	protected LineProperty newDefaultLayoutProperty() {
		return new LineProperty(0, new Margins(0));
	}

	protected void layoutHorizontal() {
		// total available size of the container
		int newSize = 0;
		int fixedSize = 0;

		newSize = getContainer().getWidth();
		fixedSize = getContainer().getHeight();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final GeoTransformableRectangle child : getManagedChildren()) {
			final int childWeight = getLayoutProperty(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if (childWeight == 0) {
				newSize -= child.toGeoTransformation().getWidth1();
			}
			totalWeightedChildSizes += childWeight;
		}

		// calculate scale to apply to the desired new size of a child
		if (totalWeightedChildSizes == 0) {
			// make scale = 1
			totalWeightedChildSizes = newSize;
		}
		final double scale = newSize / totalWeightedChildSizes;

		// new place of the next child
		int newPlace = 0;
		if (this.inverseDirection) {
			newPlace = getContainer().getWidth();
		}

		final GeoTransformableRectangle[] children = getManagedChildren();
		for (final GeoTransformableRectangle child : children) {
			final LineProperty layoutProperty = getLayoutProperty(child);
			int childWeight = layoutProperty.getWeight();

			double resizeFactor = scale;
			if (resizeFactor == 0) {
				resizeFactor = 1;
			}

			if (childWeight == 0) {
				resizeFactor = 1;
				childWeight = child.toGeoTransformation().getWidth1();
			}

			final int vMargins = layoutProperty.getMargins().getTop()
					+ layoutProperty.getMargins().getBottom();
			child.setHeight(fixedSize - vMargins);
			// calculate new width
			final double desiredChildWidth = childWeight * resizeFactor;
			final int newChildWidth = (int) Math.round(desiredChildWidth);

			final int hMargins = layoutProperty.getMargins().getLeft()
					+ layoutProperty.getMargins().getRight();
			child.setWidth(newChildWidth - hMargins);

			final int leftMargin = layoutProperty.getMargins().getLeft();
			final int topMargin = layoutProperty.getMargins().getTop();
			if (this.inverseDirection) {
				newPlace -= newChildWidth;
				child.setY(topMargin);
				child.setX(newPlace + leftMargin);
			} else {
				child.setY(topMargin);
				child.setX(newPlace + leftMargin);
				// calculate next child's position
				newPlace += newChildWidth;
			}

			child.doUpdateSizePlace();
		}
	}

	protected void layoutVertical() {
		int newSize = 0;
		int fixedSize = 0;

		newSize = getContainer().getHeight();
		fixedSize = getContainer().getWidth();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final GeoTransformableRectangle child : getManagedChildren()) {
			final int childWeight = getLayoutProperty(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if (childWeight == 0) {
				newSize -= child.toGeoTransformation().getHeight1();
			}
			totalWeightedChildSizes += childWeight;
		}

		// calculate scale to apply to the desired new size of a child
		if (totalWeightedChildSizes == 0) {
			// make scale = 1
			totalWeightedChildSizes = newSize;
		}
		final double scale = newSize / totalWeightedChildSizes;

		// new place of the next child
		int newPlace = 0;
		if (this.inverseDirection) {
			newPlace = getContainer().getHeight();
		}

		final GeoTransformableRectangle[] children = getManagedChildren();
		for (final GeoTransformableRectangle child : children) {
			final LineProperty layoutProperty = getLayoutProperty(child);
			int childWeight = layoutProperty.getWeight();

			double resizeFactor = scale;
			if (resizeFactor == 0) {
				resizeFactor = 1;
			}

			if (childWeight == 0) {
				resizeFactor = 1;
				childWeight = child.toGeoTransformation().getHeight1();
			}
			final int hMargins = layoutProperty.getMargins().getLeft()
					+ layoutProperty.getMargins().getRight();
			child.setWidth(fixedSize - hMargins);
			// calculate new height
			final double desiredChildHeight = childWeight * resizeFactor;

			final int newChildHeight = (int) Math.round(desiredChildHeight);

			final int vMargins = layoutProperty.getMargins().getTop()
					+ layoutProperty.getMargins().getBottom();
			child.setHeight(newChildHeight - vMargins);

			final int topMargin = layoutProperty.getMargins().getTop();
			final int leftMargin = layoutProperty.getMargins().getLeft();
			if (this.inverseDirection) {
				newPlace -= newChildHeight;
				child.setX(leftMargin);
				child.setY(newPlace + topMargin);
			} else {
				child.setX(leftMargin);
				child.setY(newPlace + topMargin);
				newPlace += newChildHeight;
			}
			child.doUpdateSizePlace();
		}
	}

	protected void layout() {
		if (isHorizontalDirection()) {
			layoutHorizontal();
		} else {
			layoutVertical();
		}
	}

	@Override
	protected void handleContainerChanged(
			final GeoTransformableRectangle container,
			final GeoTransformation transformation) {
		layout();
	}

	@Override
	protected void onChildChangeParentRequest(
			final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		child.doUpdateParent();
		layout();
	}

	@Override
	protected void onChildChangeVisibilityRequest(
			final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		child.doUpdateVisibility();
	}

	@Override
	protected void onChildLowerRequest(final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		child.doUpdateLower();
	}

	@Override
	protected void onChildMoveRequest(final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		cancelMoveResize(child);
	}

	@Override
	protected void onChildMoveResizeRequest(
			final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		if (getLayoutProperty(child).getWeight() == 0) {
			child.doUpdateSize();
			layout();
		} else {
			cancelMoveResize(child);
		}
	}

	@Override
	protected void onChildRaiseRequest(final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		child.doUpdateRaise();
	}

	@Override
	protected void onChildResizeRequest(final GeoTransformableRectangle child,
			final GeoTransformation transformation) {
		onChildMoveResizeRequest(child, transformation);
	}
}
