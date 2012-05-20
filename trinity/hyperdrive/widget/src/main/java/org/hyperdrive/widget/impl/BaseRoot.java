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

import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.geo.impl.GeoManagerDirect;
import org.hyperdrive.widget.api.Root;
import org.hyperdrive.widget.api.ViewReference;

// TODO documentation
// TODO shutdown logic
// TODO implement resizing+moving? (with XRANDR extension in x11)
/**
 * A <code>RealRoot</code> represents a <code>Widget</code> that is backed by
 * the native root window. It is the base of the
 * <code>GeoTransformableRectangle</code> tree hierarchy for a
 * <code>ManagedDisplay</code>. Multiple <code>RealRoot</code> widgets can be
 * constructed from the same <code>ManagedDisplay</code> but will represent the
 * same on-screen drawable, it is thus recommended to use the
 * {@link ManagedDisplay#getRealRootRenderArea()} method to reference the real
 * root.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class BaseRoot extends BaseWidget implements Root {

	@ViewReference
	private Root.View view;

	@Override
	public Root.View getView() {
		return view;
	}

	/**
	 * 
	 * @param managedDisplay
	 * 
	 */
	public BaseRoot(final ManagedDisplay managedDisplay) {
		super();
		setGeoManager(new GeoManagerDirect());
		setManagedDisplay(managedDisplay);
		init(getParent());
		setVisibility(true);
		requestVisibilityChange();
		syncGeoToPlatformRenderAreaGeo();
	}

	@Override
	protected void init(BaseWidget paintableParent) {
		super.init(paintableParent);
		syncGeoToPlatformRenderAreaGeo();
	}

	@Override
	public int getAbsoluteX() {
		return getX();
	}

	@Override
	public int getAbsoluteY() {
		return getY();
	}

	@Override
	public BaseRoot getParent() {
		return this;
	}
}
