package org.trinity.shell.geo.impl.event;

import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;
import org.trinity.shell.geo.api.event.GeoResizeEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class GeoResizeEventImpl extends GeoEventImpl implements GeoResizeEvent {

	@Inject
	protected GeoResizeEventImpl(	@Assisted final GeoTransformableRectangle transformableSquare,
									@Assisted final GeoTransformation transformation) {
		super(	transformableSquare,
				transformation);
	}
}