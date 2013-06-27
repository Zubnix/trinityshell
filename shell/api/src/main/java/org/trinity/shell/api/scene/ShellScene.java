package org.trinity.shell.api.scene;

import org.trinity.foundation.api.shared.AsyncListenable;

/**
 *
 */
public interface ShellScene extends AsyncListenable {
	ShellNodeParent getRootShellNodeParent();
}
