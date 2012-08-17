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
package org.trinity.shell.geo.impl.manager;

import java.util.List;

import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.event.GeoDestroyEvent;
import org.trinity.shell.geo.api.event.GeoHideRequestEvent;
import org.trinity.shell.geo.api.event.GeoLowerRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoRaiseRequestEvent;
import org.trinity.shell.geo.api.event.GeoReparentRequestEvent;
import org.trinity.shell.geo.api.event.GeoShowRequestEvent;
import org.trinity.shell.geo.api.manager.ShellLayoutProperty;
import org.trinity.shell.geo.api.manager.ShellLayoutPropertyLine;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
// TODO evaluate layout algoritm corner cases (negative values that shouldn't
// be negative. childs with size 0, ...)
// TODO refactor to reuse code and for cleaner reading
/**
 * A <code>ShellGeoManagerLine</code> provides a layout for children in a
 * horizontal or vertical line. All children will have the same height or width
 * for a horizontal or vertical layout respectively and are placed directly next
 * to each other with no overlap or spacing. The other child dimensions are
 * determined by the child's {@link LayoutPropertyLineImpl}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ShellGeoManagerLine extends AbstractShellGeoManager {

	private class ChildGeoListener {
		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildMoveResizeRequest(final GeoMoveResizeRequestEvent geoMoveResizeRequestEvent) {
			final ShellGeoNode child = geoMoveResizeRequestEvent.getSource();
			if (getLayoutProperty(child).getWeight() == 0) {
				child.doResize();
				layout();
			} else {
				cancelMoveResize(child);
			}
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildDestroyed(final GeoDestroyEvent geoDestroyEvent) {
			removeChild(geoDestroyEvent.getSource());
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildReparentRequest(final GeoReparentRequestEvent geoReparentRequestEvent) {
			geoReparentRequestEvent.getSource().doReparent();
			layout();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildShowRequest(final GeoShowRequestEvent geoShowRequestEvent) {
			geoShowRequestEvent.getSource().doShow();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildHideRequest(final GeoHideRequestEvent geoHideRequestEvent) {
			geoHideRequestEvent.getSource().doHide();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildLowerRequest(final GeoLowerRequestEvent geoLowerRequestEvent) {
			geoLowerRequestEvent.getSource().doLower();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildRaiseRequest(final GeoRaiseRequestEvent geoRaiseRequestEvent) {
			geoRaiseRequestEvent.getSource().doRaise();
		}
	}

	private final ChildGeoListener childGeoListener = new ChildGeoListener();

	private boolean horizontalDirection;
	private boolean inverseDirection;

	/**
	 * Create a new <code>ShellGeoManagerLine</code> that will use the geometry
	 * of the given container to determine the layout of the managed children.
	 * Ideally, the given container should be the same as the
	 * <code>ShellGeoNode</code> that returns this
	 * <code>ShellLayoutManager</code>.
	 * 
	 * @param container
	 * @param horizontalDirection
	 * @param inverseDirection
	 */
	@Inject
	public ShellGeoManagerLine(	@Assisted final ShellGeoNode container,
								@Assisted("horiz") final boolean horizontalDirection,
								@Assisted("inverse") final boolean inverseDirection) {
		super(container);
		setHorizontalDirection(horizontalDirection);
		setInverseDirection(inverseDirection);
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
	protected void cancelMoveResize(final ShellGeoNode square) {
		// make sure the square doesn't move or resizes
		square.cancelPendingMove();
		square.cancelPendingResize();
	}

	protected void layoutHorizontal() {
		// total available size of the container
		int newSize = 0;
		int fixedSize = 0;

		newSize = getLayoutContainer().getWidth();
		fixedSize = getLayoutContainer().getHeight();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellGeoNode child : getChildren()) {
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
			newPlace = getLayoutContainer().getWidth();
		}

		final List<ShellGeoNode> children = getChildren();
		for (final ShellGeoNode child : children) {
			final ShellLayoutPropertyLine layoutProperty = getLayoutProperty(child);
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

			child.doMoveResize();
		}
	}

	protected void layoutVertical() {
		int newSize = 0;
		int fixedSize = 0;

		newSize = getLayoutContainer().getHeight();
		fixedSize = getLayoutContainer().getWidth();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellGeoNode child : getChildren()) {
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
			newPlace = getLayoutContainer().getHeight();
		}

		final List<ShellGeoNode> children = getChildren();
		for (final ShellGeoNode child : children) {
			final ShellLayoutPropertyLine layoutProperty = getLayoutProperty(child);
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
			child.doMoveResize();
		}
	}

	@Override
	public void addChild(	final ShellGeoNode child,
							final ShellLayoutProperty layoutProperty) {
		child.addGeoEventHandler(this.childGeoListener);
		super.addChild(child, layoutProperty);
	}

	@Override
	public void removeChild(final ShellGeoNode child) {
		child.removeGeoEventHandler(this.childGeoListener);
		super.removeChild(child);
	}

	@Override
	public void layout() {
		if (this.horizontalDirection) {
			layoutHorizontal();
		} else {
			layoutVertical();
		}
	}

	@Subscribe
	public void handleContainerMoveReize(final GeoMoveResizeEvent moveResizeEvent) {
		layout();
	}
}