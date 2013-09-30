package org.trinity.shellplugin.wm.view.javafx.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.TypeLiteral;
import javafx.application.Application;
import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.foundation.render.javafx.api.FXModule;
import org.trinity.foundation.render.javafx.api.FXViewReferenceProvider;

@GuiceModule
public class Module extends FXModule {
    public Module() {
        super(new Runnable() {
            @Override
            public void run() {
                Application.launch(TrinityFXApplication.class);
            }
        });
    }

    @Override
    protected void fxConfigure() {
        bind(new TypeLiteral<ListenableFuture<ViewReference>>() {
        }).toProvider(new FXViewReferenceProvider<>(DesktopView.class));
    }
}
