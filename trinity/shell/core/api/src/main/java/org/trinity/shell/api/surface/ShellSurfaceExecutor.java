package org.trinity.shell.api.surface;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.shell.api.node.ShellNodeExecutor;

public interface ShellSurfaceExecutor extends ShellNodeExecutor {
	DisplayArea getSurfacePeer();
}
