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
package org.trinity.shell.api.node.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * A <code>GeoManagerWithChildren</code> is an abstract base class for
 * <code>ShellLayoutManager</code>s that have to manage children's geometry
 * relative to other children's geometry.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @param <T>
 */
public abstract class AbstractShellLayoutManager implements ShellLayoutManager {

	private final Map<ShellNode, ShellLayoutProperty> childrenWithLayoutProperty = new LinkedHashMap<ShellNode, ShellLayoutProperty>();

	/**
	 * @param child
	 * @param layoutProperty
	 */
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

	protected abstract ShellLayoutProperty defaultLayoutProperty();

	/**
	 * @param child
	 */
	@Override
	public void removeChild(final ShellNode child) {
		this.childrenWithLayoutProperty.remove(child);
	}

	/**
	 * @param index
	 */
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

	/**
	 * @return
	 */
	@Override
	public List<ShellNode> getChildren() {
		return new ArrayList<ShellNode>(this.childrenWithLayoutProperty.keySet());
	}

	/**
	 * @param index
	 * @return
	 */
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

	@Subscribe
	public void onResizeRequest(final ShellNodeResizeRequestEvent geoEvent) {
		geoEvent.getSource().doResize();
	}

	@Subscribe
	public void onMoveRequest(final ShellNodeMoveRequestEvent geoEvent) {
		geoEvent.getSource().doMove();
	}

	@Subscribe
	public void onMoveResizeRequest(final ShellNodeMoveResizeRequestEvent geoEvent) {
		geoEvent.getSource().doMoveResize();
	}

	@Subscribe
	public void onLowerRequest(final ShellNodeLowerRequestEvent geoEvent) {
		geoEvent.getSource().doLower();
	}

	@Subscribe
	public void onRaiseRequest(final ShellNodeRaiseRequestEvent geoEvent) {
		geoEvent.getSource().doRaise();
	}

	@Subscribe
	public void onShowRequest(final ShellNodeShowRequestEvent geoEvent) {
		geoEvent.getSource().doShow();
	}

	@Subscribe
	public void onHideRequest(final ShellNodeHideRequestEvent geoEvent) {
		geoEvent.getSource().doHide();
	}

	@Subscribe
	public void onChangeParentRequest(final ShellNodeReparentRequestEvent geoEvent) {
		geoEvent.getSource().doReparent();
	}
}