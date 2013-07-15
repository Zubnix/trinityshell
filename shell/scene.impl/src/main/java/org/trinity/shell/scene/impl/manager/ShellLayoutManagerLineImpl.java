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
package org.trinity.shell.scene.impl.manager;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.onami.autobind.annotations.To.Type.CUSTOM;

import java.util.List;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.shared.Margins;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.AbstractShellNode;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMovedResizedEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;
import org.trinity.shell.api.scene.manager.AbstractShellLayoutManager;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutProperty;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;

// TODO try to avoid castings
// TODO refactor/rewrite
// TODO evaluate layout algoritm corner cases (negative values that shouldn't
// be negative. childs with size 0, ...)
// TODO refactor to reuse code and for cleaner reading
// =>rewrite this sh*t...
@Bind
@To(value = CUSTOM, customs = ShellLayoutManagerLine.class)
public class ShellLayoutManagerLineImpl extends AbstractShellLayoutManager implements ShellLayoutManagerLine {

	private final ShellLayoutPropertyLine DEFAULT_LAYOUT_PROPERTY = new ShellLayoutPropertyLine(1,
																								new Margins(0));
	private final ChildGeoListener childGeoListener = new ChildGeoListener();
	private boolean horizontalDirection = true;
	private boolean inverseDirection = false;

	@Override
	public void setHorizontalDirection(final boolean horizontalDirection) {
		this.horizontalDirection = horizontalDirection;
	}

	@Override
	public void setInverseDirection(final boolean inverseDirection) {
		this.inverseDirection = inverseDirection;
	}

	protected void cancelMoveResize(final ShellNode square) {
		// make sure the square doesn't move or resizes
		square.cancelPendingMove();
		square.cancelPendingResize();
	}

	protected void layoutHorizontal(final ShellNode containerNode) {
		// total available size of the container
		checkArgument(containerNode instanceof AbstractShellNode);

		int newWidthSize = 0;
		int fixedHeightSize = 0;
		final Size size = ((AbstractShellNode) containerNode).getSizeImpl();
		newWidthSize = size.getWidth();
		fixedHeightSize = size.getHeight();

		if (newWidthSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellNode child : getChildren()) {
			final int childWeight = getLayoutProperty(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if (childWeight == 0) {
				newWidthSize -= ((AbstractShellNode) child).toGeoTransformationImpl().getRect1().getSize().getWidth();
			}
			totalWeightedChildSizes += childWeight;
		}

		// calculate scale to apply to the desired new size of a child
		if (totalWeightedChildSizes == 0) {
			// make scale = 1
			totalWeightedChildSizes = newWidthSize;
		}
		final double scale = newWidthSize / totalWeightedChildSizes;

		// new place of the next child
		int newPlace = 0;
		if (this.inverseDirection) {
			newPlace = ((AbstractShellNode) containerNode).getSizeImpl().getWidth();
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
				childWeight = ((AbstractShellNode) child).toGeoTransformationImpl().getRect1().getSize().getWidth();
			}

			final int vMargins = layoutProperty.getMargins().getTop() + layoutProperty.getMargins().getBottom();
			// calculate new width
			final double desiredChildWidth = childWeight * resizeFactor;
			final int newChildWidth = (int) Math.round(desiredChildWidth);

			final int hMargins = layoutProperty.getMargins().getLeft() + layoutProperty.getMargins().getRight();

			((AbstractShellNode) child).setSizeImpl(newChildWidth - hMargins,
													fixedHeightSize - vMargins);

			final int leftMargin = layoutProperty.getMargins().getLeft();
			final int topMargin = layoutProperty.getMargins().getTop();
			if (this.inverseDirection) {
				newPlace -= newChildWidth;
				((AbstractShellNode) child).setPositionImpl(newPlace + leftMargin,
															topMargin);
			} else {
				((AbstractShellNode) child).setPositionImpl(newPlace + leftMargin,
															topMargin);
				// calculate next child's position
				newPlace += newChildWidth;
			}

			child.doMoveResize();
		}
	}

	protected void layoutVertical(final ShellNode containerNode) {
		int newHeightSize = 0;
		int fixedWidthSize = 0;

		newHeightSize = ((AbstractShellNode) containerNode).getSizeImpl().getHeight();
		fixedWidthSize = ((AbstractShellNode) containerNode).getSizeImpl().getWidth();

		if (newHeightSize == 0) {
			return;
		}

		// total size of all children
		double totalWeightedChildSizes = 0;

		for (final ShellNode child : getChildren()) {
			final int childWeight = getLayoutProperty(child).getWeight();
			// we don't want to include children with 0 weight in the scale
			// calculation since they are treated as constants
			if (childWeight == 0) {
				newHeightSize -= ((AbstractShellNode) child).toGeoTransformationImpl().getRect1().getSize().getHeight();
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
			newPlace = ((AbstractShellNode) containerNode).getSizeImpl().getHeight();
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
				childWeight = ((AbstractShellNode) child).toGeoTransformationImpl().getRect1().getSize().getHeight();
			}
			final int hMargins = layoutProperty.getMargins().getLeft() + layoutProperty.getMargins().getRight();
			// calculate new height
			final double desiredChildHeight = childWeight * resizeFactor;

			final int newChildHeight = (int) Math.round(desiredChildHeight);

			final int vMargins = layoutProperty.getMargins().getTop() + layoutProperty.getMargins().getBottom();

			((AbstractShellNode) child).setSizeImpl(fixedWidthSize - hMargins,
													newChildHeight - vMargins);

			final int topMargin = layoutProperty.getMargins().getTop();
			final int leftMargin = layoutProperty.getMargins().getLeft();
			if (this.inverseDirection) {
				newPlace -= newChildHeight;
				((AbstractShellNode) child).setPositionImpl(leftMargin,
															newPlace + topMargin);
			} else {
				((AbstractShellNode) child).setPositionImpl(leftMargin,
															newPlace + topMargin);
				newPlace += newChildHeight;
			}
			child.doMoveResize();
		}
	}

	@Override
	public void addChildNode(	final ShellNode child,
								final ShellLayoutProperty layoutProperty) {
		Preconditions.checkArgument(layoutProperty instanceof ShellLayoutPropertyLine);

		child.register(this.childGeoListener);
		super.addChildNode(	child,
							layoutProperty);
	}

	@Override
	public void removeChild(final ShellNode child) {
		child.register(this.childGeoListener);
		super.removeChild(child);
	}

	@Override
	public void layout(final ShellNodeParent containerNode) {
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
	public ShellLayoutPropertyLine getLayoutProperty(final ShellNode child) {
		return (ShellLayoutPropertyLine) super.getLayoutProperty(child);
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
		public void handleChildMoveResizeRequest(final ShellNodeMoveResizeRequestEvent shellNodeMoveResizeRequestEvent) {
			final ShellNode child = shellNodeMoveResizeRequestEvent.getSource();
			checkArgument(child instanceof AbstractShellNode);
			if (getLayoutProperty(child).getWeight() == 0) {
				child.doResize();
				layout(((AbstractShellNode) child).getParentImpl());
			} else {
				cancelMoveResize(child);
			}
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildDestroyed(final ShellNodeDestroyedEvent shellNodeDestroyedEvent) {
			final ShellNode child = shellNodeDestroyedEvent.getSource();
			checkArgument(child instanceof AbstractShellNode);
			removeChild(shellNodeDestroyedEvent.getSource());
			layout(((AbstractShellNode) child).getParentImpl());
		}

		@SuppressWarnings("unused")
		@Subscribe
		public void handleChildReparentRequest(final ShellNodeReparentRequestEvent shellNodeReparentRequestEvent) {
			final ShellNode child = shellNodeReparentRequestEvent.getSource();
			checkArgument(child instanceof AbstractShellNode);
			final ShellNodeParent oldParent = ((AbstractShellNode) child).getParentImpl();
			shellNodeReparentRequestEvent.getSource().doReparent();
			layout(oldParent);
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
