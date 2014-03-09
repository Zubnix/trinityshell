package org.trinity.shell.scene.api.manager;

import org.trinity.shell.api.scene.ShellNodeParent;

/**
 *
 */
public interface ShellLayoutManagerFactory {
       ShellLayoutManagerLine createShellLayoutManagerLine(ShellNodeParent shellNodeParent);
}