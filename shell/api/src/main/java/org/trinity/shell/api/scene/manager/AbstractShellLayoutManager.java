/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.scene.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.scene.event.ShellNodeShowRequestEvent;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;

// TODO documentation
/**
 * Abstract base class for a {@link ShellLayoutManager}.
 */
// TODO make threadSafe
@NotThreadSafe
public abstract class AbstractShellLayoutManager implements ShellLayoutManager {

	private final Map<ShellNode, ShellLayoutProperty> childrenWithLayoutProperty = new LinkedHashMap<ShellNode, ShellLayoutProperty>();

	@Override
	public void addChildNode(@Nonnull final ShellNode child,
                             @Nonnull final ShellLayoutProperty layoutProperty) {
		this.childrenWithLayoutProperty.put(child,
											layoutProperty);
	}

	@Override
	public void addChildNode(@Nonnull final ShellNode child) {
		addChildNode(	child,
						defaultLayoutProperty());
	}

	/***************************************
	 * Return a new default layout property for this layout manager.
	 *
	 * @return a {@link ShellLayoutProperty}.
	 ***************************************
	 */
	protected abstract ShellLayoutProperty defaultLayoutProperty();

	@Override
	public void removeChild(@Nonnull final ShellNode child) {
		this.childrenWithLayoutProperty.remove(child);
	}

	@Override
	public void removeChild(final int index) {

		final Iterator<Entry<ShellNode, ShellLayoutProperty>> it = this.childrenWithLayoutProperty.entrySet()
				.iterator();

		final int i = 0;
		while (it.hasNext()) {
			final Entry<ShellNode, ShellLayoutProperty> entry = it.next();
			if (i == index) {
				removeChild(entry.getKey());
				break;
			}
		}
	}

	@Override
	public List<ShellNode> getChildren() {
		return new ArrayList<ShellNode>(this.childrenWithLayoutProperty.keySet());
	}

	@Override
	public ShellNode getChild(final int index) {
		Preconditions.checkArgument(index >= 0);

		final Iterator<Entry<ShellNode, ShellLayoutProperty>> it = this.childrenWithLayoutProperty.entrySet()
				.iterator();

		ShellNode child = null;
		for (int i = 0; it.hasNext(); i++, child = it.next().getKey()) {
			if (i == index) {
				break;
			}
		}
		return child;
	}

	@Override
	public ShellLayoutProperty getLayoutProperty(@Nonnull final ShellNode child) {

		return this.childrenWithLayoutProperty.get(child);
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be resized.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeResizeRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onResizeRequest(final ShellNodeResizeRequestEvent geoEvent) {
		geoEvent.getSource().doResize();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be moved.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeMoveRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onMoveRequest(final ShellNodeMoveRequestEvent geoEvent) {
		geoEvent.getSource().doMove();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be moved and resized.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeMoveResizeRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onMoveResizeRequest(final ShellNodeMoveResizeRequestEvent geoEvent) {
		geoEvent.getSource().doMoveResize();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be lowered.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeLowerRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onLowerRequest(final ShellNodeLowerRequestEvent geoEvent) {
		geoEvent.getSource().doLower();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests a be raised.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeRaiseRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onRaiseRequest(final ShellNodeRaiseRequestEvent geoEvent) {
		geoEvent.getSource().doRaise();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be shown.
	 *
	 * @param geoEvent
	 *            The child {@link ShowRequest}.
	 ***************************************
	 */
	@Subscribe
	public void onShowRequest(final ShellNodeShowRequestEvent geoEvent) {
		geoEvent.getSource().doShow();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be hidden.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeHideRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onHideRequest(final ShellNodeHideRequestEvent geoEvent) {
		geoEvent.getSource().doHide();
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be reparented.
	 *
	 * @param geoEvent
	 *            The child {@link ShellNodeReparentRequestEvent}.
	 ***************************************
	 */
	@Subscribe
	public void onChangeParentRequest(final ShellNodeReparentRequestEvent geoEvent) {
		geoEvent.getSource().doReparent();
	}
}