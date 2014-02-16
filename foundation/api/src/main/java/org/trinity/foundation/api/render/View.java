package org.trinity.foundation.api.render;


import org.trinity.foundation.api.display.DisplaySurface;

public interface View {

    Object getBindableView();

    DisplaySurface getViewDisplaySurface();
}
