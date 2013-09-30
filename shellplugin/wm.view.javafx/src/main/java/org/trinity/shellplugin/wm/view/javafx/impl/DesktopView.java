package org.trinity.shellplugin.wm.view.javafx.impl;


import com.cathive.fx.guice.GuiceFXMLLoader;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class DesktopView extends FXView {

    @Inject
    DesktopView(@Nonnull GuiceFXMLLoader loader) {
        super(loader);
    }
}
