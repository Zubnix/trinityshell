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
package org.trinity.shell.geo.api.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.event.GeoHideRequestEvent;
import org.trinity.shell.geo.api.event.GeoLowerRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoRaiseRequestEvent;
import org.trinity.shell.geo.api.event.GeoReparentRequestEvent;
import org.trinity.shell.geo.api.event.GeoResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoShowRequestEvent;

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

	private final Map<ShellGeoNode, ShellLayoutProperty> childrenWithLayoutProperty = new LinkedHashMap<ShellGeoNode, ShellLayoutProperty>();
	private ShellGeoNode container;

	/**
	 * @return
	 */
	@Override
	public ShellGeoNode getLayoutContainer() {
		return this.container;
	}

	@Override
	public void setLayoutContainer(final ShellGeoNode layoutContainer) {
		this.container = layoutContainer;
	}

	/**
	 * @param child
	 * @param layoutProperty
	 */
	@Override
	public void addChild(	final ShellGeoNode child,
							final ShellLayoutProperty layoutProperty) {
		this.childrenWithLayoutProperty.put(child, layoutProperty);
	}

	@Override
	public void addChild(final ShellGeoNode child) {
		addChild(child, defaultLayoutProperty());
	}

	protected abstract ShellLayoutProperty defaultLayoutProperty();

	/**
	 * @param child
	 */
	@Override
	public void removeChild(final ShellGeoNode child) {
		this.childrenWithLayoutProperty.remove(child);
	}

	/**
	 * @param index
	 */
	@Override
	public void removeChild(final int index) {

		final Iterator<Entry<ShellGeoNode, ShellLayoutProperty>> it = this.childrenWithLayoutProperty
				.entrySet().iterator();

		final int i = 0;
		while (it.hasNext()) {
			final Entry<ShellGeoNode, ShellLayoutProperty> entry = it.next();
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
	public List<ShellGeoNode> getChildren() {
		return new ArrayList<ShellGeoNode>(this.childrenWithLayoutProperty.keySet());
	}

	/**
	 * @param index
	 * @return
	 */
	@Override
	public ShellGeoNode getChild(final int index) {
		final Iterator<Entry<ShellGeoNode, ShellLayoutProperty>> it = this.childrenWithLayoutProperty
				.entrySet().iterator();

		ShellGeoNode child = null;
		for (int i = 0; it.hasNext(); i++, child = it.next().getKey()) {
			if (i == index) {
				break;
			}
		}
		return child;
	}

	/**
	 * @param child
	 * @return
	 */
	@Override
	public ShellLayoutPropertyLine getLayoutProperty(final ShellGeoNode child) {
		return (ShellLayoutPropertyLine) this.childrenWithLayoutProperty
				.get(child);
	}

	@Subscribe
	public void onResizeRequest(final GeoResizeRequestEvent geoEvent) {
		geoEvent.getSource().doResize();
	}

	@Subscribe
	public void onMoveRequest(final GeoMoveRequestEvent geoEvent) {
		geoEvent.getSource().doMove();
	}

	@Subscribe
	public void onMoveResizeRequest(final GeoMoveResizeRequestEvent geoEvent) {
		geoEvent.getSource().doMoveResize();
	}

	@Subscribe
	public void onLowerRequest(final GeoLowerRequestEvent geoEvent) {
		geoEvent.getSource().doLower();
	}

	@Subscribe
	public void onRaiseRequest(final GeoRaiseRequestEvent geoEvent) {
		geoEvent.getSource().doRaise();
	}

	@Subscribe
	public void onShowRequest(final GeoShowRequestEvent geoEvent) {
		geoEvent.getSource().doShow();
	}

	@Subscribe
	public void onHideRequest(final GeoHideRequestEvent geoEvent) {
		geoEvent.getSource().doHide();
	}

	@Subscribe
	public void onChangeParentRequest(final GeoReparentRequestEvent geoEvent) {
		geoEvent.getSource().doReparent();
	}
}