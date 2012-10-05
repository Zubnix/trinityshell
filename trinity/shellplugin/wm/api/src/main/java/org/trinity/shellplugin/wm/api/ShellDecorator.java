package org.trinity.shellplugin.wm.api;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.widget.ShellWidget;

public interface ShellDecorator {
	/****************************************
	 * 
	 * @param client
	 * @return {@link ShellNode} that should be used in place of the given
	 *         client. Can be null.
	 *************************************** 
	 */
	ShellNode decorateClientSurface(ShellSurface client);

	/***************************************
	 * @param root
	 * @return {@link ShellNodeParent} that should be used as the client's
	 *         parent.
	 *************************************** 
	 */
	ShellNodeParent decorateRootShellWidget(ShellWidget root);
}
