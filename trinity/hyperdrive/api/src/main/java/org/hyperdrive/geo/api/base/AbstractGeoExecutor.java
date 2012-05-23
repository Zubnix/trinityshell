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
package org.hyperdrive.geo.api.base;

import org.hyperdrive.geo.api.GeoExecutor;
import org.trinity.core.display.api.Area;
import org.trinity.core.display.api.AreaManipulator;

// TODO documentation
/**
 * An <code>AbstractGeoExecutor</code> is an abstract base delegate class for
 * executing geometry requests that originated from a
 * <code>AbstractRenderArea</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * @see GeoExecutor
 */
public abstract class AbstractGeoExecutor implements GeoExecutor {

	protected AbstractGeoExecutor() {
		// protected to reduce visibility and encourage dependency injection
	}

	/**
	 * 
	 * @return
	 */
	public abstract <T extends Area> AreaManipulator<T> getAreaManipulator();

	@Override
	public void lower() {
		this.getAreaManipulator().lower();
	}

	@Override
	public void raise() {
		this.getAreaManipulator().raise();
	}

	@Override
	public void updateSize(final int width, final int height) {
		this.getAreaManipulator().resize(width, height);
	}

	@Override
	public void updateVisibility(final boolean visible) {
		if (visible) {
			this.getAreaManipulator().show();
		} else {
			this.getAreaManipulator().hide();
		}
	}

	/**
	 * 
	 * @param newParentArea
	 * @param newX
	 * @param newY
	 */
	protected void reparent(final Area newParentArea, final int newX,
			final int newY) {
		this.getAreaManipulator().setParent(newParentArea, newX, newY);
	}
}
