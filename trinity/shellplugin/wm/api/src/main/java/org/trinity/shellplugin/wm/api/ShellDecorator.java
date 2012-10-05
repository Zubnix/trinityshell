package org.trinity.shellplugin.wm.api;

import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.widget.ShellWidget;

public interface ShellDecorator {
	void decorateClientSurface(ShellSurface client);

	void decorateRootShellWidget(ShellWidget root);
}
