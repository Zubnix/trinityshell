package org.trinity.shell.scene.api.manager;

import org.trinity.shell.scene.api.ShellNodeParent;

/**
 *
 */
public interface ShellLayoutManagerFactory {
       ShellLayoutManagerLine createShellLayoutManagerLine(ShellNodeParent shellNodeParent);
}