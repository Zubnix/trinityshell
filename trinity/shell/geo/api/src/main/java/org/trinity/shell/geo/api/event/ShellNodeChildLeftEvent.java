package org.trinity.shell.geo.api.event;

import org.trinity.shell.geo.api.ShellNode;
import org.trinity.shell.geo.api.ShellNodeTransformation;

public class ShellNodeChildLeftEvent extends ShellNodeChildEvent {

	public ShellNodeChildLeftEvent(	final ShellNode shellNode,
								final ShellNodeTransformation shellNodeTransformation) {
		super(shellNode, shellNodeTransformation);
	}
}