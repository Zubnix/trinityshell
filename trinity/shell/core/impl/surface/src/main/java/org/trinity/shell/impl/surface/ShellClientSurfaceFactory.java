package org.trinity.shell.impl.surface;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.api.surface.ShellSurface;

public interface ShellClientSurfaceFactory {
	ShellSurface createShellClientSurface(DisplaySurface displaySurface);
}
