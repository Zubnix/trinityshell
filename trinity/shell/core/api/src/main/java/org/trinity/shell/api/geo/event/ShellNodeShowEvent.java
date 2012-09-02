package org.trinity.shell.api.geo.event;

import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeTransformation;

public class ShellNodeShowEvent extends ShellNodeVisibilityEvent {

	public ShellNodeShowEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}