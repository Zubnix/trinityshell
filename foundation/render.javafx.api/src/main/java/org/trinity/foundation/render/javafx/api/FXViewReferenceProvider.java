package org.trinity.foundation.render.javafx.api;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.application.Platform;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.AbstractViewReferenceProvider;
import org.trinity.foundation.api.render.ViewReference;

import javax.annotation.Nonnull;

public class FXViewReferenceProvider<V extends FXView> extends AbstractViewReferenceProvider<V> {

    @Inject
    private Injector injector;

    private final Class<V> fxViewClass;

    public FXViewReferenceProvider(@Nonnull final Class<V> fxViewClass) {
        this.fxViewClass = fxViewClass;
    }

    @Override
    protected void invokeViewBuild(final ListenableFutureTask<V> viewFuture) {
        Platform.runLater(viewFuture);
    }

    @Override
    protected ViewReference createViewReference(final V createdViewObject,
                                                final DisplaySurface displaySurface) {
        return new ViewReference() {
            @Override
            public V getView() {
                return createdViewObject;
            }

            @Override
            public DisplaySurface getViewDisplaySurface() {
                return displaySurface;
            }
        };
    }

    @Override
    protected V createViewObject() {
        return injector.getInstance(this.fxViewClass);
    }

    @Override
    protected DisplaySurfaceHandle createDisplaySurfaceHandle(final V createdViewObject) {
        return new FXDisplaySurfaceHandle(createdViewObject);
    }
}
