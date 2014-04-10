package org.trinity.x11.defaul;

import org.trinity.common.Listenable;

import javax.annotation.Nonnull;

/**
 *
 */
public interface XCompositor {
    @Nonnull
    Listenable create(@Nonnull Integer nativeHandle);
}
