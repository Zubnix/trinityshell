package org.trinity.shellplugin.wm.x11.impl.scene;

import org.trinity.shell.api.surface.ShellSurface;

public interface ClientBarElementFactory {

	ClientBarElement createClientTopBarItem(ShellSurface client);
}
