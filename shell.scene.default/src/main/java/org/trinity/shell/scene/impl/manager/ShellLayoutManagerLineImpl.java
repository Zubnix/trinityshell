/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.scene.impl.manager;

import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellNodeParent;
import org.trinity.shell.scene.api.event.ShellNodeDestroyedEvent;
import org.trinity.shell.scene.api.event.ShellNodeHideRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.scene.api.event.ShellNodeShowRequestEvent;
import org.trinity.shell.scene.api.manager.ShellLayoutManagerLine;
import org.trinity.shell.scene.api.manager.ShellLayoutProperty;
import org.trinity.shell.scene.api.manager.ShellLayoutPropertyLine;
import org.trinity.shell.scene.impl.ShellNodeImpl;
import org.trinity.shell.scene.impl.ShellNodeParentImpl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.Insets;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

// TODO try to avoid castings
// TODO refactor/rewrite
// TODO evaluate layout algoritm corner cases (negative values that shouldn't
// be negative. childs with size 0, ...)
// TODO refactor to reuse code and for cleaner reading
// =>rewrite this sh*t...
@NotThreadSafe
public class ShellLayoutManagerLineImpl extends AbstractShellLayoutManager implements ShellLayoutManagerLine {

	private static final ShellLayoutPropertyLine DEFAULT_LAYOUT_PROPERTY = new ShellLayoutPropertyLine(1,
            Insets.getZero());
	private final        ChildGeoListener        childGeoListener        = new ChildGeoListener();
	private              boolean                 horizontalDirection     = true;
	private              boolean                 inverseDirection        = false;

	ShellLayoutManagerLineImpl(//TODO with autofactory
							   final ShellNodeParent shellNodeParent) {
		super((ShellNodeParentImpl) shellNodeParent);
	}

	@Override
	public void setHorizontalDirection(final boolean horizontalDirection) {
		this.horizontalDirection = horizontalDirection;
	}

	@Override
	public void setInverseDirection(final boolean inverseDirection) {
		this.inverseDirection = inverseDirection;
	}

	protected void cancelMoveResize(@Nonnull final ShellSurface square) {
		// make sure the square doesn't move or resizes
		square.cancelPendingMove();
		square.cancelPendingResize();
	}

	protected void layoutHorizontal(@Nonnull final ShellSurface containerNode) {
		// total available size of the container
		checkArgument(containerNode instanceof ShellNodeImpl);

		int newWidthSize = 0;
		int fixedHeightSize = 0;
		final Dimension size = containerNode.getSize();
		newWidthSize = size.getWidth();
		fixedHeightSize = size.getHeight();

		if(newWidthSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for(final ShellSurface child : getChildren()) {
			final int childWeight = getChildLayoutConfiguration(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if(childWeight == 0) {
				newWidthSize -= child.toGeoTransformation().getRect1().getWidth();
			}
			totalWeightedChildSizes += childWeight;
		}

		// calculate scale to apply to the desired new size of a child
		if(totalWeightedChildSizes == 0) {
			// make scale = 1
			totalWeightedChildSizes = newWidthSize;
		}
		final double scale = newWidthSize / totalWeightedChildSizes;

		// new place of the next child
		int newPlace = 0;
		if(this.inverseDirection) {
			newPlace = containerNode.getSize().getWidth();
		}

		final List<ShellSurface> children = getChildren();
		for(final ShellSurface child : children) {
			final ShellLayoutPropertyLine layoutProperty = getChildLayoutConfiguration(child);
			int childWeight = layoutProperty.getWeight();

			double resizeFactor = scale;
			if(resizeFactor == 0) {
				resizeFactor = 1;
			}

			if(childWeight == 0) {
				resizeFactor = 1;
				childWeight = child.toGeoTransformation().getRect1().getWidth();
			}

			final int vMargins = layoutProperty.getMargins().getTopHeight() + layoutProperty.getMargins().getBottomHeight();
			// calculate new width
			final double desiredChildWidth = childWeight * resizeFactor;
			final int newChildWidth = (int) Math.round(desiredChildWidth);

			final int hMargins = layoutProperty.getMargins().getLeftWidth() + layoutProperty.getMargins().getRightWidth();

			 child.setSize(newChildWidth - hMargins,
                     fixedHeightSize - vMargins);

			final int leftMargin = layoutProperty.getMargins().getLeftWidth();
			final int topMargin = layoutProperty.getMargins().getTopHeight();
			if (this.inverseDirection) {
				newPlace -= newChildWidth;
				 child.setPosition(newPlace + leftMargin,
                         topMargin);
			} else {
				 child.setPosition(newPlace + leftMargin,
                         topMargin);
				// calculate next child's position
				newPlace += newChildWidth;
			}

			child.doMove();
            child.doResize();
		}
	}

	protected void layoutVertical(@Nonnull final ShellSurface containerNode) {
		int newHeightSize = 0;
		int fixedWidthSize = 0;

		newHeightSize = containerNode.getSize().getHeight();
		fixedWidthSize = containerNode.getSize().getWidth();

		if (newHeightSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellSurface child : getChildren()) {
			final int childWeight = getChildLayoutConfiguration(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if (childWeight == 0) {
				newHeightSize -= child.toGeoTransformation().getRect1().getHeight();
			}
			totalWeightedChildSizes += childWeight;
		}

		// calculate scale to apply to the desired new size of a child
		if (totalWeightedChildSizes == 0) {
			// make scale = 1
			totalWeightedChildSizes = newHeightSize;
		}
		final double scale = newHeightSize / totalWeightedChildSizes;

		// new place of the next child
		int newPlace = 0;
		if (this.inverseDirection) {
			newPlace = containerNode.getSize().getHeight();
		}

		final List<ShellSurface> children = getChildren();
		for (final ShellSurface child : children) {
			final ShellLayoutPropertyLine layoutProperty = getChildLayoutConfiguration(child);
			int childWeight = layoutProperty.getWeight();

			double resizeFactor = scale;
			if (resizeFactor == 0) {
				resizeFactor = 1;
			}

			if (childWeight == 0) {
				resizeFactor = 1;
				childWeight =  child.toGeoTransformation().getRect1().getHeight();
			}
			final int hMargins = layoutProperty.getMargins().getLeftWidth() + layoutProperty.getMargins().getRightWidth();
			// calculate new height
			final double desiredChildHeight = childWeight * resizeFactor;

			final int newChildHeight = (int) Math.round(desiredChildHeight);

			final int vMargins = layoutProperty.getMargins().getTopHeight() + layoutProperty.getMargins().getBottomHeight();

			child.setSize(fixedWidthSize - hMargins,
                    newChildHeight - vMargins);

			final int topMargin = layoutProperty.getMargins().getTopHeight();
			final int leftMargin = layoutProperty.getMargins().getLeftWidth();
			if (this.inverseDirection) {
				newPlace -= newChildHeight;
				child.setPosition(leftMargin,
                        newPlace + topMargin);
			} else {
				 child.setPosition(leftMargin,
                         newPlace + topMargin);
				newPlace += newChildHeight;
			}
			child.doMove();
            child.doResize();
		}
	}

	@Override
	public void setChildLayoutConfiguration(@Nonnull final ShellSurface child,
											@Nonnull final ShellLayoutProperty layoutProperty) {
		checkArgument(layoutProperty instanceof ShellLayoutPropertyLine);

		child.register(this.childGeoListener);
		super.setChildLayoutConfiguration(child,
										  layoutProperty);
	}

	@Override
	public void removeChildLayoutConfiguration(@Nonnull final ShellSurface child) {
		child.register(this.childGeoListener);
		super.removeChildLayoutConfiguration(child);
	}

	@Override
	public void layout(@Nonnull final ShellNodeParent containerNode) {
		if (containerNode == null) {
			return;
		}
		if (this.horizontalDirection) {
			layoutHorizontal(containerNode);
		} else {
			layoutVertical(containerNode);
		}
	}

	@Override
	public ShellLayoutPropertyLine getChildLayoutConfiguration(@Nonnull final ShellSurface child) {
		return (ShellLayoutPropertyLine) super.getChildLayoutConfiguration(child);
	}

	@Subscribe
	public void handleContainerMoveResize(final ShellNodeMovedResizedEvent moveResizeEvent) {
		layout((ShellNodeParent) moveResizeEvent.getSource());
	}

	@Override
	public ShellLayoutPropertyLine defaultLayoutProperty() {
		return this.DEFAULT_LAYOUT_PROPERTY;
	}

	private class ChildGeoListener {
		// unused methods are used by guava's eventbus.

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildDestroyed(final ShellNodeDestroyedEvent shellNodeDestroyedEvent) {
			removeChildLayoutConfiguration(shellNodeDestroyedEvent.getSource());
			layout(getShellNodeParent());
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildReparentRequest(final ShellNodeReparentRequestEvent shellNodeReparentRequestEvent) {
			shellNodeReparentRequestEvent.getSource().doReparent();
			layout(getShellNodeParent());
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildShowRequest(final ShellNodeShowRequestEvent shellNodeShowRequestEvent) {
			shellNodeShowRequestEvent.getSource().doShow();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildHideRequest(final ShellNodeHideRequestEvent shellNodeHideRequestEvent) {
			shellNodeHideRequestEvent.getSource().doHide();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildLowerRequest(final ShellNodeLowerRequestEvent shellNodeLowerRequestEvent) {
			shellNodeLowerRequestEvent.getSource().doLower();
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildRaiseRequest(final ShellNodeRaiseRequestEvent shellNodeRaiseRequestEvent) {
			shellNodeRaiseRequestEvent.getSource().doRaise();
		}
	}
}
