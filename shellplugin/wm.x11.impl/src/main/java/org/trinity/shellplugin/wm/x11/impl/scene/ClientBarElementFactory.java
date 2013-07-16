package org.trinity.shellplugin.wm.x11.impl.scene;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shell.api.surface.ShellSurface;

public interface ClientBarElementFactory {

	ClientBarElement createClientTopBarItem(final DisplaySurface clientXWindow);
}
