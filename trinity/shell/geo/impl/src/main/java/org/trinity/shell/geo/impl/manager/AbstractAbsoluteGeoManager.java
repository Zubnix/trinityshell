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

import org.trinity.shell.geo.api.event.GeoHideRequestEvent;
import org.trinity.shell.geo.api.event.GeoLowerRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveRequestEvent;
import org.trinity.shell.geo.api.event.GeoMoveResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoRaiseRequestEvent;
import org.trinity.shell.geo.api.event.GeoReparentRequestEvent;
import org.trinity.shell.geo.api.event.GeoResizeRequestEvent;
import org.trinity.shell.geo.api.event.GeoShowRequestEvent;
import org.trinity.shell.geo.api.manager.GeoManager;

import com.google.common.eventbus.Subscribe;

public abstract class AbstractAbsoluteGeoManager implements GeoManager {

	@Subscribe
	public void onResizeRequest(final GeoResizeRequestEvent geoEvent) {
		geoEvent.getSource().doResize();
	}

	@Subscribe
	public void onMoveRequest(final GeoMoveRequestEvent geoEvent) {
		geoEvent.getSource().doMove();
	}

	@Subscribe
	public void onMoveResizeRequest(final GeoMoveResizeRequestEvent geoEvent) {
		geoEvent.getSource().doMoveResize();
	}

	@Subscribe
	public void onLowerRequest(final GeoLowerRequestEvent geoEvent) {
		geoEvent.getSource().doLower();
	}

	@Subscribe
	public void onRaiseRequest(final GeoRaiseRequestEvent geoEvent) {
		geoEvent.getSource().doRaise();
	}

	@Subscribe
	public void onShowRequest(final GeoShowRequestEvent geoEvent) {
		geoEvent.getSource().doShow();
	}

	@Subscribe
	public void onHideRequest(final GeoHideRequestEvent geoEvent) {
		geoEvent.getSource().doHide();
	}

	@Subscribe
	public void onChangeParentRequest(final GeoReparentRequestEvent geoEvent) {
		geoEvent.getSource().doReparent();
	}
}