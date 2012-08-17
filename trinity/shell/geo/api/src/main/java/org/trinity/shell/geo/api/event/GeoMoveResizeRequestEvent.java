package org.trinity.shell.geo.api.event;

import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.ShellGeoTransformation;

public class GeoMoveResizeRequestEvent extends GeoEvent {

	public GeoMoveResizeRequestEvent(	final ShellGeoNode shellGeoNode,
										final ShellGeoTransformation shellGeoTransformation) {
		super(shellGeoNode, shellGeoTransformation);
	}
}