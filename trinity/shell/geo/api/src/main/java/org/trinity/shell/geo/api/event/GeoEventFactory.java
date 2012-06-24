/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.api.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface GeoEventFactory {
			GeoDestroyEvent
			createGeoDestroyEvent(	@Assisted GeoTransformableRectangle transformableSquare,
									@Assisted GeoTransformation transformation);

			GeoHideEvent
			createGeoHideEvent(	@Assisted GeoTransformableRectangle transformableSquare,
								@Assisted GeoTransformation transformation);

			GeoHideRequestEvent
			createGeoHideRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoLowerEvent
			createGeoLowerEvent(@Assisted GeoTransformableRectangle transformableSquare,
								@Assisted GeoTransformation transformation);

			GeoLowerRequestEvent
			createGeoLowerRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoMoveEvent
			createGeoMoveEvent(	@Assisted GeoTransformableRectangle transformableSquare,
								@Assisted GeoTransformation transformation);

			GeoMoveRequestEvent
			createGeoMoveRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoMoveResizeEvent
			createGeoMoveResizeEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoMoveResizeRequestEvent
			createGeoMoveResizeRequestEvent(@Assisted GeoTransformableRectangle transformableSquare,
											@Assisted GeoTransformation transformation);

			GeoRaiseEvent
			createGeoRaiseEvent(@Assisted GeoTransformableRectangle transformableSquare,
								@Assisted GeoTransformation transformation);

			GeoRaiseRequestEvent
			createGeoRaiseRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoReparentEvent
			createGeoReparentEvent(	@Assisted GeoTransformableRectangle transformableSquare,
									@Assisted GeoTransformation transformation);

			GeoReparentRequestEvent
			createGeoReparentRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
											@Assisted GeoTransformation transformation);

			GeoResizeEvent
			createGeoResizeEvent(	@Assisted GeoTransformableRectangle transformableSquare,
									@Assisted GeoTransformation transformation);

			GeoResizeRequestEvent
			createGeoResizeRequestEvent(@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoShowEvent
			createGeoShowEvent(	@Assisted GeoTransformableRectangle transformableSquare,
								@Assisted GeoTransformation transformation);

			GeoShowRequestEvent
			createGeoShowRequestEvent(	@Assisted GeoTransformableRectangle transformableSquare,
										@Assisted GeoTransformation transformation);

			GeoChildAddedEvent
			createGeoChildAddedEvent(	@Assisted GeoTransformableRectangle geoTransformableRectangle,
										@Assisted GeoTransformation geoTransformation);

			GeoChildLeftEvent
			createGeoChildLeftEvent(@Assisted GeoTransformableRectangle geoTransformableRectangle,
									@Assisted GeoTransformation geoTransformation);
}
