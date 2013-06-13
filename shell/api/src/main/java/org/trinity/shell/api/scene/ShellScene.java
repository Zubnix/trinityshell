package org.trinity.shell.api.scene;

import org.trinity.foundation.api.shared.AsyncListenable;

/**
 *
 */
public interface ShellScene extends AsyncListenable{
    void addShellNode(ShellNode shellNode);

    void removeShellNode(ShellNode shellNode);
}
