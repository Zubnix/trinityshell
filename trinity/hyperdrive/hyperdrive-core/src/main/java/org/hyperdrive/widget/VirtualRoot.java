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
package org.hyperdrive.widget;

import org.hyperdrive.geo.GeoManagerDirect;

//TODO documentation
//TODO list events that are emitted by this widget in doc
/**
 * A <code>VirtualRoot</code> is a <code>Widget</code> that can be used as a
 * virtual desktop for a <code>ManagedDisplay</code>. Unlike the
 * <code>RealRoot<code>, there can be more than one <code>VirtualRoot</code>,
 * each referencing a different unique native window.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class VirtualRoot extends Widget {

	/**
	 * 
	 * @param realRoot
	 */
	public VirtualRoot(final RealRoot realRoot) {
		setParent(realRoot);
		setRelativeX(realRoot.getRelativeX());
		setRelativeY(realRoot.getRelativeY());
		setWidth(realRoot.getWidth());
		setHeight(realRoot.getHeight());
		setGeoManager(new GeoManagerDirect());
		requestReparent();
		requestMoveResize();
	}

	@Override
	protected View initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newVirtualRootView();
	}

	/**
	 * Fires a {@link VirtualRootEvent} on this <code>VirtualRoot</code> with
	 * the <code>VIRTUAL_ROOT_ACTIVATED</code> type. Programmers should handle
	 * this event appropriately as this method does nothing else.
	 */
	public void activate() {
		// TODO activate virtualroot
		fireEvent(new VirtualRootEvent(VirtualRootEvent.VIRTUAL_ROOT_ACTIVATED,
				this));
	}
}
