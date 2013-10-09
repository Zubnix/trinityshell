package org.trinity.foundation.render.javafx.api;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.scene.Node;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.AbstractViewBuilder;

import javax.inject.Inject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class FXViewBuilder extends AbstractViewBuilder {

    //HACK HACK HACK... JavaFX doesn't provide api to check if the application is started or not...
    private static final Lock PLATFORM_STARTED_LOCK_HACK = new ReentrantLock(true);
    public static volatile boolean PLATFORM_STARTED_HACK = false;

    @Inject
    private Injector injector;

    private Class<? extends FXView> viewClass;

    public FXViewBuilder(Class<? extends FXView> viewClass) {
        this.viewClass = viewClass;
    }

    @Override
    protected void invokeViewBuild(final ListenableFutureTask<Object> viewFuture) {
        PLATFORM_STARTED_LOCK_HACK.lock();
        try {
            if(PLATFORM_STARTED_HACK) {
                Platform.runLater(viewFuture);
            } else {
                viewFuture.run();
            }
        } finally {
            PLATFORM_STARTED_LOCK_HACK.unlock();
        }
    }

    @Override
    protected Object createViewObject() {
        return injector.getInstance(this.viewClass);
    }

    @Override
    protected DisplaySurfaceHandle createDisplaySurfaceHandle(final Object createdViewObject) {
        return new FXDisplaySurfaceHandle((Node) createdViewObject);
    }
}
