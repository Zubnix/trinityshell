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
package org.trinity.shell.geo.impl;

import org.trinity.core.display.api.Area;
import org.trinity.core.display.api.AreaManipulator;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;

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
	 * @return
	 */
	public abstract <T extends Area> AreaManipulator<T> getAreaManipulator(GeoTransformableRectangle geoTransformableRectangle);

	@Override
	public void lower(final GeoTransformableRectangle geoTransformableRectangle) {
		this.getAreaManipulator(geoTransformableRectangle).lower();
	}

	@Override
	public void raise(final GeoTransformableRectangle geoTransformableRectangle) {
		this.getAreaManipulator(geoTransformableRectangle).raise();
	}

	@Override
	public void updateSize(	final GeoTransformableRectangle geoTransformableRectangle,
							final int width,
							final int height) {
		this.getAreaManipulator(geoTransformableRectangle)
				.resize(width, height);
	}

	@Override
	public void updateVisibility(	final GeoTransformableRectangle geoTransformableRectangle,
									final boolean visible) {
		if (visible) {
			this.getAreaManipulator(geoTransformableRectangle).show();
		} else {
			this.getAreaManipulator(geoTransformableRectangle).hide();
		}
	}

	/**
	 * @param newParentArea
	 * @param newX
	 * @param newY
	 */
	protected void reparent(final GeoTransformableRectangle geoTransformableRectangle,
							final Area newParentArea,
							final int newX,
							final int newY) {
		getAreaManipulator(geoTransformableRectangle).setParent(newParentArea,
																newX,
																newY);
	}
}
