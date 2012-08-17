package org.trinity.shell.geo.api.event;

import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.geo.api.ShellGeoTransformation;

public class GeoChildAddedEvent extends GeoChildEvent {

	public GeoChildAddedEvent(	final ShellGeoNode shellGeoNode,
								final ShellGeoTransformation shellGeoTransformation) {
		super(shellGeoNode, shellGeoTransformation);
	}
}