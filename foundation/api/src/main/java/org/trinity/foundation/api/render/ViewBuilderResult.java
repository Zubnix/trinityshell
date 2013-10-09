package org.trinity.foundation.api.render;


import org.trinity.foundation.api.display.DisplaySurface;

public interface ViewBuilderResult {

    void onResult(Object bindableView,
                  DisplaySurface viewDisplaySurface);
}
