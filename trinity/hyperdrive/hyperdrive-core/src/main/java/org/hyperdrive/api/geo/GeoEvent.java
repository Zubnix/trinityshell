package org.hyperdrive.api.geo;

import org.hydrogen.api.event.Event;

public interface GeoEvent extends Event<GeoOperation> {
	GeoTransformableRectangle getGeoTransformableRectangle();

	GeoTransformation getGeoTransformation();
}
