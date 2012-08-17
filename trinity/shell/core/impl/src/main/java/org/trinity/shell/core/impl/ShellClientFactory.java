package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplayRenderArea;

public interface ShellClientFactory {
	ShellClient createShellClient(DisplayRenderArea displayRenderArea);
}
