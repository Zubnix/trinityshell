package org.trinity.shell.geo.api.manager;

import java.util.List;

import org.trinity.shell.geo.api.ShellNode;

public interface ShellLayoutManager {

	ShellNode getLayoutContainer();

	void setContainerShellSurface(ShellNode layoutContainer);

	void addChild(ShellNode child);

	void addChildShellSurface(	final ShellNode child,
					final ShellLayoutProperty layoutProperty);

	ShellLayoutProperty getLayoutProperty(final ShellNode child);

	ShellNode getChild(final int index);

	List<ShellNode> getChildren();

	void removeChild(final ShellNode child);

	void removeChild(final int index);

	void layout();
}