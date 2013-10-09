package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.AbstractModule;
import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.ViewBuilder;
import org.trinity.foundation.render.javafx.api.FXViewBuilder;

@GuiceModule
public class Module extends AbstractModule {

    @Override
    protected void configure() {

        FXViewBuilder desktopViewBuilder = new FXViewBuilder(org.trinity.shellplugin.wm.view.javafx.DesktopView.class);
        requestInjection(desktopViewBuilder);
        bind(ViewBuilder.class).annotatedWith(org.trinity.shellplugin.wm.api.viewkey.DesktopView.class).toInstance(desktopViewBuilder);
    }
}
