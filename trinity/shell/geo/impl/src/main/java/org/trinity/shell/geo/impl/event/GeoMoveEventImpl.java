package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoMoveEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoMoveEventImpl extends GeoEventImpl implements GeoMoveEvent {

	@Inject
	protected GeoMoveEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
								@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}