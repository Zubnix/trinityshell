package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

public interface Buffer extends HasSize<BufferSpace>{

    void accept(@Nonnull Object renderCommand);
}
