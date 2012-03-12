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
package org.hyperdrive.geo;

import org.hyperdrive.api.geo.GeoManager;
import org.hyperdrive.api.geo.HasGeoManager;

// TODO documentation
/**
 * A <code>GeoVirtRectangle</code> is a 'virtual', invisible rectangle with a
 * size, place and visibility and has no visual representation on-screen. A
 * <code>GeoVirtRectangle</code> has the same behavior as a
 * <code>GeoTransformableRectangle</code>. This means that when a
 * <code>GeoVirtRectangle</code> moves, all it's children will move with the
 * same offset. When a <code>GeoVirtRectangle</code> visibility changes, all
 * it's children's visibility will change as well. A
 * <code>GeoVirtRectangle</code> can also have an optional
 * <code>GeoManager</code> which means it can manage it's children's geometry
 * requests if necessary.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GeoVirtRectangle extends AbstractGeoTransformableRectangle
		implements HasGeoManager {
	private final GeoVirtGeoExecutor geoExecutor;
	private GeoManager geoManager;

	/**
	 * 
	 */
	public GeoVirtRectangle() {
		this.geoExecutor = new GeoVirtGeoExecutor(this);
		setGeoManager(new GeoManagerDirect());
	}

	@Override
	public GeoVirtGeoExecutor getGeoExecutor() {
		return this.geoExecutor;
	}

	@Override
	public GeoManager getGeoManager() {
		return this.geoManager;
	}

	/**
	 * 
	 * @param geoManager
	 */
	public void setGeoManager(final GeoManager geoManager) {
		this.geoManager = geoManager;
	}
}
