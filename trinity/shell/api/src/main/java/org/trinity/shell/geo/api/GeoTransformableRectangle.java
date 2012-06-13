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
package org.trinity.shell.geo.api;

import org.trinity.foundation.render.api.HierarchicalRectangle;
import org.trinity.shell.geo.api.manager.GeoManager;

public interface GeoTransformableRectangle extends HierarchicalRectangle,
		GeoTransformable {

	void cancelPendingMove();

	void cancelPendingResize();

	void doDestroy();

	void doUpdateLower();

	void doUpdateParent();

	void doUpdatePlace();

	void doUpdateRaise();

	void doUpdateSizePlace();

	void doUpdateSize();

	void doUpdateVisibility();

	GeoTransformableRectangle[] getChildren();

	void notifyChildAdded(GeoTransformableRectangle newChild);

	GeoExecutor getGeoExecutor();

	boolean isDestroyed();

	void requestLower();

	void requestMove();

	void requestMoveResize();

	void requestRaise();

	void requestReparent();

	void requestResize();

	void requestVisibilityChange();

	void setHeight(final int height);

	void setParent(final GeoTransformableRectangle parent);

	void setX(final int x);

	void setY(final int y);

	void setVisibility(final boolean visible);

	void setWidth(final int width);

	GeoManager getParentGeoManager();

	@Override
	GeoTransformableRectangle getParent();

	void addGeoEventHandler(GeoEventHandler geoEventHandler);

	void removeGeoEventHandler(GeoEventHandler geoEventHandler);

	/**
	 * Handles a child's geometry request and will be notified of this
	 * <code>GeoTransformableRectangle</code> geometry changes.
	 * 
	 * @return The <code>GeoManger</code> that implements a certain layout.
	 */
	GeoManager getGeoManager();

	void setGeoManager(GeoManager geoManager);
}
