package org.trinity.shellplugin.wm.view.javafx.impl;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import javafx.application.Platform;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.render.AbstractViewReferenceProvider;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.shellplugin.wm.view.javafx.api.FXDisplaySurfaceHandle;
import org.trinity.shellplugin.wm.view.javafx.api.TrinityFXController;

import javax.annotation.Nonnull;

public abstract class AbstractFXViewReferenceProvider extends AbstractViewReferenceProvider {

    private final GuiceFXMLLoader fxmlLoader;

    @Inject
    AbstractFXViewReferenceProvider(@Nonnull @DisplayExecutor final ListeningExecutorService displayExecutor,
                                    @Nonnull final DisplaySurfacePool displaySurfacePool,
                                    final GuiceFXMLLoader fxmlLoader) {
        super(displayExecutor,
                displaySurfacePool);
        this.fxmlLoader = fxmlLoader;
    }

    @Override
    protected void invokeViewBuild(final ListenableFutureTask<Object> viewFuture) {
        Platform.runLater(viewFuture);
    }

    @Override
    protected abstract GuiceFXMLLoader.Result createViewObject();

    @Override
    protected ViewReference createViewReference(final Object createdViewObject, final DisplaySurface displaySurface) {
        final GuiceFXMLLoader.Result guiceFXMLResult = (GuiceFXMLLoader.Result) createdViewObject;
        final TrinityFXController controller = guiceFXMLResult.getController();
        return new ViewReference() {
            @Override
            public Object getView() {
                return controller;
            }

            @Override
            public DisplaySurface getViewDisplaySurface() {
                return displaySurface;
            }
        };
    }

    @Override
    protected DisplaySurfaceHandle createDisplaySurfaceHandle(Object createdViewObject) {
        final GuiceFXMLLoader.Result guiceFXMLResult = (GuiceFXMLLoader.Result) createdViewObject;
        final TrinityFXController controller = guiceFXMLResult.getController();

        return new FXDisplaySurfaceHandle(controller.getControlledNode());
    }
}
