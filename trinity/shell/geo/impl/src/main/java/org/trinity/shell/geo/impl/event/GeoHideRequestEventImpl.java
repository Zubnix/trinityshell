package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoHideRequestEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoHideRequestEventImpl extends GeoEventImpl implements
		GeoHideRequestEvent {

	@Inject
	protected GeoHideRequestEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
										@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}
