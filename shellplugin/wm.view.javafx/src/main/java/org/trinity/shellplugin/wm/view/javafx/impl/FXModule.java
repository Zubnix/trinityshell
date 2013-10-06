package org.trinity.shellplugin.wm.view.javafx.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.foundation.render.javafx.api.FXViewReferenceProvider;
import org.trinity.shellplugin.wm.api.viewreferencekey.DesktopViewReference;

@GuiceModule
public class FXModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<ListenableFuture<ViewReference>>() {
        }).annotatedWith(DesktopViewReference.class).toProvider(new FXViewReferenceProvider<>(FXDesktopView.class));
    }
}