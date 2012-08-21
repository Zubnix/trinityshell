package org.trinity.shell.widget.api.view;

import org.trinity.shell.core.api.ShellSurface;

public interface ShellClientManagerView extends ShellWidgetView {
	void addClient(ShellSurface client);

	void removeClient(ShellSurface client);
}