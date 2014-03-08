package org.trinity.shell.api.scene;

import javax.annotation.Nonnull;

public interface ShellNodeParentFactory {

	ShellNodeParent construct(@Nonnull ShellNodeParent parent);
}
