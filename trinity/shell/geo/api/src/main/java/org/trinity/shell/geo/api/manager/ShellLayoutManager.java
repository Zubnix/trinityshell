package org.trinity.shell.geo.api.manager;

import java.util.List;

import org.trinity.shell.geo.api.ShellGeoNode;

public interface ShellLayoutManager {

	ShellGeoNode getLayoutContainer();

	void setLayoutContainer(ShellGeoNode layoutContainer);

	void addChild(ShellGeoNode child);

	void addChild(	final ShellGeoNode child,
					final ShellLayoutProperty layoutProperty);

	ShellLayoutProperty getLayoutProperty(final ShellGeoNode child);

	ShellGeoNode getChild(final int index);

	List<ShellGeoNode> getChildren();

	void removeChild(final ShellGeoNode child);

	void removeChild(final int index);

	void layout();

	ShellLayoutProperty newLayoutProperty();
}