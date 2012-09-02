package org.trinity.shell.api.geo.event;

import org.trinity.shell.api.geo.ShellNode;
import org.trinity.shell.api.geo.ShellNodeTransformation;

public class ShellNodeStackingRequestEvent extends ShellNodeEvent {

	public ShellNodeStackingRequestEvent(	final ShellNode shellNode,
											final ShellNodeTransformation shellNodeTransformation) {
		super(	shellNode,
				shellNodeTransformation);
	}

}
