package org.trinity.foundation.api.render;

import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.foundation.api.display.DisplaySurface;

/**
 *
 */
public interface ViewReference {
    Object getView();

    DisplaySurface getViewDisplaySurface();
}
