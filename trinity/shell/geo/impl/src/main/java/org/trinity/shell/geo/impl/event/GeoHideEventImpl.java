package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoHideEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoHideEventImpl extends GeoEventImpl implements GeoHideEvent {

	@Inject
	protected GeoHideEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
								@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}
