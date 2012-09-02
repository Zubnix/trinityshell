package org.trinity.shell.api.geo.event;

import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeTransformation;

public class ShellNodeHideEvent extends ShellNodeVisibilityEvent {

	public ShellNodeHideEvent(final ShellNode shellNode, final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}
}