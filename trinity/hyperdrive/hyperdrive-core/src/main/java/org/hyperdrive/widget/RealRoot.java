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

import org.hydrogen.api.display.PlatformRenderArea;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.widget.ViewDefinition;
import org.hyperdrive.geo.GeoManagerDirect;

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
public final class RealRoot extends BaseWidget {

	@ViewDefinition
	public interface View extends BaseWidget.View {

	}

	/**
	 * 
	 * @param managedDisplay
	 * 
	 */
	public RealRoot(final ManagedDisplay managedDisplay) {
		super();
		setGeoManager(new GeoManagerDirect());
		setManagedDisplay(managedDisplay);
		init(getParent());
		setVisibility(true);
		requestVisibilityChange();
		syncGeoToPlatformRenderAreaGeo();
		// requestMoveResize();
	}

	@Override
	public int getAbsoluteX() {
		final int returnint = getX();
		return returnint;
	}

	@Override
	public int getAbsoluteY() {
		final int returnint = getY();
		return returnint;
	}

	@Override
	public RealRoot getParent() {
		return this;
	}

	@Override
	protected void setPlatformRenderArea(
			final PlatformRenderArea platformRenderArea) {
		super.setPlatformRenderArea(platformRenderArea);
		syncGeoToPlatformRenderAreaGeo();
	}
}
