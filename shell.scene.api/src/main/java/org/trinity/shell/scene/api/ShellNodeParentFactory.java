package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

public interface ShellNodeParentFactory {

	ShellNodeParent construct(@Nonnull ShellNodeParent parent);
}
