package org.trinity.shell.geo.api.event;

import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.ShellGeoTransformation;

public class GeoVisibilityRequestEvent extends GeoEvent {

	public GeoVisibilityRequestEvent(	final ShellGeoNode shellGeoNode,
										final ShellGeoTransformation shellGeoTransformation) {
		super(shellGeoNode, shellGeoTransformation);
	}
}