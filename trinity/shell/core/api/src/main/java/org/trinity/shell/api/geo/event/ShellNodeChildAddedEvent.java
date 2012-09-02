package org.trinity.shell.api.geo.event;

import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeTransformation;

public class ShellNodeChildAddedEvent extends ShellNodeChildEvent {

	public ShellNodeChildAddedEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}