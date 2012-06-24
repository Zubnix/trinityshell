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
package org.trinity.shell.geo.impl.manager;

import org.trinity.shell.geo.api.event.GeoEvent;
import org.trinity.shell.geo.api.manager.GeoManager;

public class GeoManagerDirect implements GeoManager {

	@Override
	public void onResizeRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doUpdateSize();
	}

	@Override
	public void onMoveRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doUpdatePlace();
	}

	@Override
	public void onMoveResizeRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doUpdateSizePlace();
	}

	@Override
	public void onLowerRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doLower();
	}

	@Override
	public void onRaiseRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doRaise();
	}

	@Override
	public void onShowRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doShow();
	}

	@Override
	public void onHideRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doHide();
	}

	@Override
	public void onChangeParentRequest(final GeoEvent geoEvent) {
		geoEvent.getSource().doUpdateParent();
	}

	@Override
	public void onResizeNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onMoveNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onMoveResizeNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onLowerNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onRaiseNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onHideNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onShowNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onChangeParentNotify(final GeoEvent geoEvent) {
		// do nothing
	}

	@Override
	public void onDestroyNotify(final GeoEvent geoEvent) {
		// do nothin.
	}
}