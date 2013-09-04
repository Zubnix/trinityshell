package org.trinity.shell.api.scene.manager;

import org.trinity.shell.api.scene.ShellNodeParent;

/**
 *
 */
public interface ShellLayoutManagerFactory {
       ShellLayoutManagerLine createShellLayoutManagerLine(ShellNodeParent shellNodeParent);
}