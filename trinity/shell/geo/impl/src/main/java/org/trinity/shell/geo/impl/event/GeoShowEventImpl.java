package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoShowEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoShowEventImpl extends GeoEventImpl implements GeoShowEvent {

	@Inject
	protected GeoShowEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
								@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}