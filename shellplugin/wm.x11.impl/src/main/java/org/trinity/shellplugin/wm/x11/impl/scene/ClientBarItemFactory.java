package org.trinity.shellplugin.wm.x11.impl.scene;

import org.trinity.shell.api.surface.ShellSurface;

public interface ClientBarItemFactory {

	ClientBarItem createClientTopBarItem(ShellSurface client);
}
