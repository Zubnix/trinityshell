package org.trinity.shellplugin.wm.view.javafx.impl;


import com.cathive.fx.guice.GuiceFXMLLoader;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind(to = @To(IMPLEMENTATION))
public class FXDesktopView extends FXView {

    @Inject
    FXDesktopView(@Nonnull final GuiceFXMLLoader loader) {
        super(loader);
    }
}
