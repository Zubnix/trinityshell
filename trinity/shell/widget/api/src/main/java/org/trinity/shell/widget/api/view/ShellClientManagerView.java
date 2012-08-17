package org.trinity.shell.widget.api.view;

import org.trinity.shell.core.api.ShellRenderArea;

public interface ShellClientManagerView extends ShellWidgetView {
	void addClient(ShellRenderArea client);

	void removeClient(ShellRenderArea client);
}