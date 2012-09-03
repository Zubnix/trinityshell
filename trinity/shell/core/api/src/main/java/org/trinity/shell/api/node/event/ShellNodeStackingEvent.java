package org.trinity.shell.api.node.event;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeTransformation;

public class ShellNodeStackingEvent extends ShellNodeEvent {

	public ShellNodeStackingEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}