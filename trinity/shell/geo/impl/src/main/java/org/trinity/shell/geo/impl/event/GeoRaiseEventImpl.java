package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoRaiseEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoRaiseEventImpl extends GeoEventImpl implements GeoRaiseEvent {

	@Inject
	protected GeoRaiseEventImpl(@Assisted final GeoTransformableRectangle transformableSquare,
								@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}