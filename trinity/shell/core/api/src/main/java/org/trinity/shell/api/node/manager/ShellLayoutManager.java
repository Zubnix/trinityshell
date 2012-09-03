package org.trinity.shell.api.node.manager;

import java.util.List;

import org.trinity.shell.api.node.ShellNode;

public interface ShellLayoutManager {

	void addChildNode(ShellNode child);

	void addChildNode(	final ShellNode child,
						final ShellLayoutProperty layoutProperty);

	ShellLayoutProperty getLayoutProperty(final ShellNode child);

	ShellNode getChild(final int index);

	List<ShellNode> getChildren();

	void removeChild(final ShellNode child);

	void removeChild(final int index);

	void layout(ShellNode containerNode);
}