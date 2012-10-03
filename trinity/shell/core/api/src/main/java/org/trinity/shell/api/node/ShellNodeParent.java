package org.trinity.shell.api.node;

import org.trinity.shell.api.node.manager.ShellLayoutManager;

public interface ShellNodeParent extends ShellNode {
	ShellLayoutManager getLayoutManager();

	void layout();

	void setLayoutManager(ShellLayoutManager shellLayoutManager);

	ShellNode[] getChildren();

	void handleChildReparentEvent(ShellNode child);
}
