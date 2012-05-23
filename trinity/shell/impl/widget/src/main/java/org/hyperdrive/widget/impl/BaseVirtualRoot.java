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
package org.hyperdrive.widget.impl;

import org.hyperdrive.geo.impl.GeoManagerDirect;
import org.hyperdrive.widget.api.ViewReference;
import org.hyperdrive.widget.api.VirtualRoot;

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
public final class BaseVirtualRoot extends BaseWidget implements VirtualRoot {

	@ViewReference
	private VirtualRoot.View view;

	@Override
	public VirtualRoot.View getView() {
		return view;
	}

	/**
	 * 
	 * @param baseRoot
	 */
	public BaseVirtualRoot(final BaseRoot baseRoot) {
		setParent(baseRoot);
		setX(baseRoot.getX());
		setY(baseRoot.getY());
		setWidth(baseRoot.getWidth());
		setHeight(baseRoot.getHeight());
		setGeoManager(new GeoManagerDirect());
		requestReparent();
		requestMoveResize();
	}
}
