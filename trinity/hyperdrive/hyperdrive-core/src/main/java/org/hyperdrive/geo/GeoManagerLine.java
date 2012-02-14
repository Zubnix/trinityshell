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

import org.hydrogen.eventsystem.EventHandler;

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
public class GeoManagerLine extends GeoManagerWithChildren<LineProperty> {

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
		registerEventHandlers();
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
	 */
	protected void registerEventHandlers() {
		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();
				cancelMoveResize(square);
			}
		}, GeoEvent.MOVE_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();
				cancelMoveResize(square);
			}
		}, GeoEvent.RESIZE_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();
				cancelMoveResize(square);
			}
		}, GeoEvent.MOVE_RESIZE_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();

				square.doUpdateRaise();
			}
		}, GeoEvent.RAISE_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();

				square.doUpdateLower();
			}
		}, GeoEvent.LOWER_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();
				square.doUpdateVisibility();
			}
		}, GeoEvent.VISIBILITY_REQUEST);

		addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				final GeoTransformableRectangle square = event.getTransformableSquare();
				removeManagedChild(square);
				square.doUpdateParentValue();
			}
		}, GeoEvent.REPARENT_REQUEST);

		getContainer().addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				layout();
			}
		}, GeoEvent.RESIZE);

		getContainer().addEventHandler(new EventHandler<GeoEvent>() {
			@Override
			public void handleEvent(final GeoEvent event) {
				layout();
			}
		}, GeoEvent.MOVE_RESIZE);
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
	public void addManagedChild(final GeoTransformableRectangle child) {
		addManagedChild(child, new LineProperty(0));
	}

	@Override
	public void addManagedChild(final GeoTransformableRectangle child,
			final LineProperty layoutProperty) {
		super.addManagedChild(child, layoutProperty);
		layout();
	}

	@Override
	public void removeManagedChild(final GeoTransformableRectangle child) {
		super.removeManagedChild(child);
		layout();
	}

	/**
	 * 
	 * 
	 *
	 * 
	 */
	// TODO refactor
	protected void layout() {
		// total available size of the container
		int newSize = 0;
		int fixedSize = 0;
		if (this.horizontalDirection) {
			newSize = getContainer().getWidth();
			fixedSize = getContainer().getHeight();
		} else {
			newSize = getContainer().getHeight();
			fixedSize = getContainer().getWidth();
		}
		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final GeoTransformableRectangle child : getAllChildren()) {
			final int childWeight = getLayoutProperty(child).getWeight();
			if (this.horizontalDirection) {
				// we don't want to include children with 0 weight in the scale
				// calculation since they are treated as constants
				if (childWeight == 0) {
					newSize -= child.toGeoTransformation().getWidth1();
				}
				totalWeightedChildSizes += childWeight;
			} else {
				// we don't want to include children with 0 weight in the scale
				// calculation since they are treated as constants
				if (childWeight == 0) {
					newSize -= child.toGeoTransformation().getHeight1();
				}
				totalWeightedChildSizes += childWeight;
			}
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
			if (this.horizontalDirection) {
				newPlace = getContainer().getWidth();
			} else {
				newPlace = getContainer().getHeight();
			}
		}
		final GeoTransformableRectangle[] children = getAllChildren();
		for (final GeoTransformableRectangle child : children) {

			int childWeight = getLayoutProperty(child).getWeight();

			double resizeFactor = scale;
			if (resizeFactor == 0) {
				resizeFactor = 1;
			}

			if (this.horizontalDirection) {
				if (childWeight == 0) {
					resizeFactor = 1;

					childWeight = child.toGeoTransformation().getWidth1();
				}
				child.setHeight(fixedSize);
				// calculate new width
				final double desiredChildWidth = childWeight * resizeFactor;
				final int newChildWidth = (int) Math.round(desiredChildWidth);
				child.setWidth(newChildWidth);

				if (this.inverseDirection) {
					newPlace -= newChildWidth;
					child.setRelativeX(newPlace);
				} else {
					child.setRelativeX(newPlace);
					// calculate next child's position
					newPlace += newChildWidth;
				}
			} else {
				if (childWeight == 0) {
					resizeFactor = 1;
					childWeight = child.toGeoTransformation().getHeight1();
				}
				child.setWidth(fixedSize);
				// calculate new height
				final double desiredChildHeight = childWeight * resizeFactor;

				final int newChildHeight = (int) Math.round(desiredChildHeight);
				child.setHeight(newChildHeight);

				if (this.inverseDirection) {
					newPlace -= newChildHeight;
					child.setRelativeY(newPlace);
				} else {
					child.setRelativeY(newPlace);
					newPlace += newChildHeight;
				}
			}
			child.doUpdateSizePlaceValue();
		}
	}
}
