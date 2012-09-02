package org.trinity.shell.api.geo.event;

import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeTransformation;

public class ShellNodeReparentEvent extends ShellNodeEvent {

	public ShellNodeReparentEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}