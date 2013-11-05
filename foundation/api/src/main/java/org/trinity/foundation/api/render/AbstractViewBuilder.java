package org.trinity.foundation.api.render;


import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;

import javax.inject.Inject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.google.common.util.concurrent.ListenableFutureTask.create;

public abstract class AbstractViewBuilder implements ViewBuilder {

    @Inject
    @DisplayExecutor
    private ListeningExecutorService displayExecutor;
    @Inject
    private DisplaySurfacePool displaySurfacePool;

    private final ListenableFutureTask<Object> viewFuture = create(new Callable<Object>() {
        @Override
        public Object call() {
            return createViewObject();
        }
    });

    @Override
    public ListenableFuture<Object[]> build(final ViewBuilderResult viewBuildResult) {
        return displayExecutor.submit(new Callable<Object[]>() {
            @Override
            public Object[] call() throws ExecutionException, InterruptedException {
                try(DisplaySurfaceCreator displaySurfaceCreator = displaySurfacePool.getDisplaySurfaceCreator()) {
                    invokeViewBuild(viewFuture);
                    final Object createdViewObject = viewFuture.get();
                    DisplaySurfaceHandle createdDisplaySurfaceHandle = createDisplaySurfaceHandle(createdViewObject);
                    final DisplaySurface displaySurface = displaySurfaceCreator
                            .reference(createdDisplaySurfaceHandle);
                    viewBuildResult.onResult(createdViewObject,
                                             displaySurface);
                    return new Object[]{createdViewObject,
                                        displaySurface};
                }
            }
        });
    }

    protected abstract void invokeViewBuild(ListenableFutureTask<Object> viewFuture);

    protected abstract Object createViewObject();

    protected abstract DisplaySurfaceHandle createDisplaySurfaceHandle(Object createdViewObject);
}
