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
package org.trinity.shell.api.node.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.trinity.foundation.display.api.event.ShowRequestEvent;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.event.ShellNodeHideRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeLowerRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeMoveResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeRaiseRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeReparentRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeResizeRequestEvent;
import org.trinity.shell.api.node.event.ShellNodeShowRequestEvent;

import com.google.common.eventbus.Subscribe;

// TODO documentation
/**
 * Abstract base class for a {@link ShellLayoutManager}.
 */
public abstract class AbstractShellLayoutManager implements ShellLayoutManager {

	private final Map<ShellNode, ShellLayoutProperty> childrenWithLayoutProperty = new LinkedHashMap<ShellNode, ShellLayoutProperty>();

	@Override
	public void addChildNode(	final ShellNode child,
								final ShellLayoutProperty layoutProperty) {
		this.childrenWithLayoutProperty.put(child,
											layoutProperty);
	}

	@Override
	public void addChildNode(final ShellNode child) {
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
	public void removeChild(final ShellNode child) {
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
	public ShellLayoutPropertyLine getLayoutProperty(final ShellNode child) {
		return (ShellLayoutPropertyLine) this.childrenWithLayoutProperty.get(child);
	}

	/***************************************
	 * Called when a child {@link ShellNode} requests to be resized.
	 * 
	 * @param geoEvent
	 *            A {@link ShellNodeResizeRequestEvent}.
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
	 *            A {@link ShellNodeMoveRequestEvent}.
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
	 *            A {@link ShellNodeMoveResizeRequestEvent}.
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
	 *            A {@link ShellNodeLowerRequestEvent}.
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
	 *            A {@link ShellNodeRaiseRequestEvent}.
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
	 *            A {@link ShowRequestEvent}.
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
	 *            A {@link ShellNodeHideRequestEvent}.
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
	 *            A {@link ShellNodeReparentRequestEvent}.
	 *************************************** 
	 */
	@Subscribe
	public void onChangeParentRequest(final ShellNodeReparentRequestEvent geoEvent) {
		geoEvent.getSource().doReparent();
	}
}