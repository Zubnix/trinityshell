package org.trinity.foundation.render.javafx.api;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.scene.Node;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.AbstractViewBuilder;

import javax.inject.Inject;


public class FXViewBuilder extends AbstractViewBuilder {

    @Inject
    private Injector injector;

    private Class<? extends FXView> viewClass;

    public FXViewBuilder(Class<? extends FXView> viewClass) {
        this.viewClass = viewClass;
    }

    @Override
    protected void invokeViewBuild(final ListenableFutureTask<Object> viewFuture) {

        if(Platform.isFxApplicationThread()) {
            viewFuture.run();
        } else {
            Platform.runLater(viewFuture);
        }
    }

    @Override
    protected Object createViewObject() {
        return this.injector.getInstance(this.viewClass);
    }

    @Override
    protected DisplaySurfaceHandle createDisplaySurfaceHandle(final Object createdViewObject) {
        return new FXDisplaySurfaceHandle((Node) createdViewObject);
    }
}
