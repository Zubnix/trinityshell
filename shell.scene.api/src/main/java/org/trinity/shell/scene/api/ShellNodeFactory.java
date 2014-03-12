package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

public interface ShellNodeFactory {

	ShellNode construct(@Nonnull ShellNode parent, @Nonnull Object buffer);
}
