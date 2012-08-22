package org.trinity.shell.widget.api.view;

import java.util.concurrent.Future;

import org.trinity.shell.core.api.ShellSurface;

public interface ShellClientManagerView extends ShellWidgetView {
	Future<Void> addClient(ShellSurface client);

	Future<Void> removeClient(ShellSurface client);
}