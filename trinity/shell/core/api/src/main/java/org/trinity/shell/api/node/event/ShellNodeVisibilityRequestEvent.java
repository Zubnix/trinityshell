package org.trinity.shell.api.node.event;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeTransformation;

public class ShellNodeVisibilityRequestEvent extends ShellNodeEvent {

	public ShellNodeVisibilityRequestEvent(	final ShellNode shellNode,
											final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}