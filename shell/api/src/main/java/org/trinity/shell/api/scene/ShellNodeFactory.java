package org.trinity.shell.api.scene;

import javax.annotation.Nonnull;

public interface ShellNodeFactory {

	ShellNode construct(@Nonnull ShellNodeParent parent);
}
