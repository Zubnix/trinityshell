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

import org.trinity.foundation.shared.geometry.api.Margins;
import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.event.ShellNodeDestroyEvent;
import org.trinity.shell.api.geo.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.geo.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.geo.event.ShellNodeMoveResizeEvent;
import org.trinity.shell.api.geo.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.geo.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.geo.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.geo.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.geo.manager.AbstractShellLayoutManager;
import org.trinity.shell.api.geo.manager.ShellLayoutManager;
import org.trinity.shell.api.geo.manager.ShellLayoutProperty;
import org.trinity.shell.api.geo.manager.ShellLayoutPropertyLine;

import com.google.common.eventbus.Subscribe;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

// TODO documentation
// TODO evaluate layout algoritm corner cases (negative values that shouldn't
// be negative. childs with size 0, ...)
// TODO refactor to reuse code and for cleaner reading
/**
 * A <code>ShellLayoutManagerLine</code> provides a layout for children in a
 * horizontal or vertical line. All children will have the same height or width
 * for a horizontal or vertical layout respectively and are placed directly next
 * to each other with no overlap or spacing. The other child dimensions are
 * determined by the child's {@link LayoutPropertyLineImpl}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
@Bind(value = @Named("ShellLayoutManagerLine"), to = @To(value = Type.CUSTOM, customs = ShellLayoutManager.class))
public class ShellLayoutManagerLine extends AbstractShellLayoutManager {

	private final ShellLayoutPropertyLine DEFAULT_LAYOUT_PROPERTY = new ShellLayoutPropertyLine(0,
																								new Margins(0));

	private class ChildGeoListener {
		@Subscribe
		public void handleChildMoveResizeRequest(final ShellNodeMoveResizeRequestEvent shellNodeMoveResizeRequestEvent) {
			final ShellNode child = shellNodeMoveResizeRequestEvent.getSource();
			if (getLayoutProperty(child).getWeight() == 0) {
				child.doResize();
				layout(child.getParent());
			} else {
				cancelMoveResize(child);
			}
		}

		@Subscribe
		public void handleChildDestroyed(final ShellNodeDestroyEvent shellNodeDestroyEvent) {
			removeChild(shellNodeDestroyEvent.getSource());
		}

		@Subscribe
		public void handleChildReparentRequest(final ShellNodeReparentRequestEvent shellNodeReparentRequestEvent) {
			final ShellNode oldParent = shellNodeReparentRequestEvent.getSource().getParent();
			shellNodeReparentRequestEvent.getSource().doReparent();
			layout(oldParent);
		}

		@Subscribe
		public void handleChildShowRequest(final ShellNodeShowRequestEvent shellNodeShowRequestEvent) {
			shellNodeShowRequestEvent.getSource().doShow();
		}

		@Subscribe
		public void handleChildHideRequest(final ShellNodeHideRequestEvent shellNodeHideRequestEvent) {
			shellNodeHideRequestEvent.getSource().doHide();
		}

		@Subscribe
		public void handleChildLowerRequest(final ShellNodeLowerRequestEvent shellNodeLowerRequestEvent) {
			shellNodeLowerRequestEvent.getSource().doLower();
		}

		@Subscribe
		public void handleChildRaiseRequest(final ShellNodeRaiseRequestEvent shellNodeRaiseRequestEvent) {
			shellNodeRaiseRequestEvent.getSource().doRaise();
		}
	}

	private final ChildGeoListener childGeoListener = new ChildGeoListener();

	private boolean horizontalDirection;
	private boolean inverseDirection;

	/**
	 * Create a new <code>ShellLayoutManagerLine</code> that will use the
	 * geometry of the given container to determine the layout of the managed
	 * children. Ideally, the given container should be the same as the
	 * <code>ShellNode</code> that returns this <code>ShellLayoutManager</code>.
	 * 
	 * @param container
	 * @param horizontalDirection
	 * @param inverseDirection
	 */
	ShellLayoutManagerLine() {
		setHorizontalDirection(true);
		setInverseDirection(false);
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
	protected void cancelMoveResize(final ShellNode square) {
		// make sure the square doesn't move or resizes
		square.cancelPendingMove();
		square.cancelPendingResize();
	}

	protected void layoutHorizontal(final ShellNode containerNode) {
		// total available size of the container
		int newSize = 0;
		int fixedSize = 0;

		newSize = containerNode.getWidth();
		fixedSize = containerNode.getHeight();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellNode child : getChildren()) {
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
			newPlace = containerNode.getWidth();
		}

		final List<ShellNode> children = getChildren();
		for (final ShellNode child : children) {
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

			final int vMargins = layoutProperty.getMargins().getTop() + layoutProperty.getMargins().getBottom();
			child.setHeight(fixedSize - vMargins);
			// calculate new width
			final double desiredChildWidth = childWeight * resizeFactor;
			final int newChildWidth = (int) Math.round(desiredChildWidth);

			final int hMargins = layoutProperty.getMargins().getLeft() + layoutProperty.getMargins().getRight();
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

	protected void layoutVertical(final ShellNode containerNode) {
		int newSize = 0;
		int fixedSize = 0;

		newSize = containerNode.getHeight();
		fixedSize = containerNode.getWidth();

		if (newSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellNode child : getChildren()) {
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
			newPlace = containerNode.getHeight();
		}

		final List<ShellNode> children = getChildren();
		for (final ShellNode child : children) {
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
			final int hMargins = layoutProperty.getMargins().getLeft() + layoutProperty.getMargins().getRight();
			child.setWidth(fixedSize - hMargins);
			// calculate new height
			final double desiredChildHeight = childWeight * resizeFactor;

			final int newChildHeight = (int) Math.round(desiredChildHeight);

			final int vMargins = layoutProperty.getMargins().getTop() + layoutProperty.getMargins().getBottom();
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
	public void addChildNode(	final ShellNode child,
								final ShellLayoutProperty layoutProperty) {
		// TODO logging
		System.err.println(String.format(	"Add child: %s to layout manager: %s",
											child,
											this));

		child.addShellNodeEventHandler(this.childGeoListener);
		super.addChildNode(	child,
							layoutProperty);
	}

	@Override
	public void removeChild(final ShellNode child) {
		child.removeShellNodeEventHandler(this.childGeoListener);
		super.removeChild(child);
	}

	@Override
	public void layout(final ShellNode containerNode) {
		if (containerNode == null) {
			return;
		}
		if (this.horizontalDirection) {
			layoutHorizontal(containerNode);
		} else {
			layoutVertical(containerNode);
		}
	}

	@Subscribe
	public void handleContainerMoveReize(final ShellNodeMoveResizeEvent moveResizeEvent) {
		layout(moveResizeEvent.getSource());
	}

	@Override
	public ShellLayoutProperty defaultLayoutProperty() {
		return this.DEFAULT_LAYOUT_PROPERTY;
	}
}