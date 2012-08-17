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

import org.trinity.foundation.render.api.RenderNode;
import org.trinity.shell.geo.api.manager.ShellLayoutManager;

public interface ShellGeoNode extends RenderNode, ShellGeoTransformable {

	void cancelPendingMove();

	void cancelPendingResize();

	void doDestroy();

	void doLower();

	void doReparent();

	void doMove();

	void doRaise();

	void doMoveResize();

	void doResize();

	void doShow();

	void doHide();

	ShellGeoNode[] getChildren();

	ShellGeoExecutor getGeoExecutor();

	boolean isDestroyed();

	@Override
	boolean isVisible();

	void requestLower();

	void requestMove();

	void requestMoveResize();

	void requestRaise();

	void requestReparent();

	void requestResize();

	void requestShow();

	void requestHide();

	void setHeight(final int height);

	void setParent(final ShellGeoNode parent);

	void setX(final int x);

	void setY(final int y);

	void setWidth(final int width);

	ShellLayoutManager getParentGeoManager();

	@Override
	ShellGeoNode getParent();

	/**
	 * Handles a child's geometry request and will be notified of this
	 * <code>ShellGeoNode</code> geometry changes.
	 * 
	 * @return The <code>GeoManger</code> that implements a certain layout.
	 */
	ShellLayoutManager getGeoManager();

	void setGeoManager(ShellLayoutManager shellLayoutManager);

	void addGeoEventHandler(Object geoEventHandler);

	void removeGeoEventHandler(Object geoEventHandler);
}
