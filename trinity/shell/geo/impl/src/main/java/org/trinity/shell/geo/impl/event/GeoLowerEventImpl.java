package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoLowerEvent;

public class GeoLowerEventImpl extends GeoEventImpl implements GeoLowerEvent {

	protected GeoLowerEventImpl(final GeoTransformableRectangle transformableSquare,
								final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}

}
