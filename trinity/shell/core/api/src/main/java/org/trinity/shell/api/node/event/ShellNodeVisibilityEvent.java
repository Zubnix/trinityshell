package org.trinity.shell.api.node.event;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeTransformation;

public class ShellNodeVisibilityEvent extends ShellNodeEvent {

	public ShellNodeVisibilityEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}