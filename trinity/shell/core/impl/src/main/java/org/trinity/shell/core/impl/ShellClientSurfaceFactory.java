package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.api.ShellSurface;

public interface ShellClientSurfaceFactory {
	ShellSurface createShellClientSurface(DisplaySurface displaySurface);
}
