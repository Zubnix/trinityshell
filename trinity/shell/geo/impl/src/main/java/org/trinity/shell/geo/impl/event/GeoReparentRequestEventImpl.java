package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoReparentRequestEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoReparentRequestEventImpl extends GeoEventImpl implements
		GeoReparentRequestEvent {

	@Inject
	protected GeoReparentRequestEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
											@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}
