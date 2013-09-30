/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.api.render;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Provider;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.google.common.util.concurrent.ListenableFutureTask.create;

public abstract class AbstractViewReferenceProvider implements Provider<ListenableFuture<ViewReference>> {

    private final ListeningExecutorService displayExecutor;
    private final DisplaySurfacePool displaySurfacePool;
    private final ListenableFutureTask<Object> viewFuture = create(new Callable<Object>() {
        @Override
        public Object call() {
            return createViewObject();
        }
    });
    private final Callable<ViewReference> viewReferenceTask = new Callable<ViewReference>() {
        @Override
        public ViewReference call() throws ExecutionException, InterruptedException {
            try (DisplaySurfaceCreator displaySurfaceCreator = displaySurfacePool.getDisplaySurfaceCreator()) {
                invokeViewBuild(viewFuture);
                final Object createdViewObject = viewFuture.get();
                DisplaySurfaceHandle createdDisplaySurfaceHandle = createDisplaySurfaceHandle(createdViewObject);
                final DisplaySurface displaySurface = displaySurfaceCreator
                        .reference(createdDisplaySurfaceHandle);
                return createViewReference(createdViewObject, displaySurface);
            }
        }
    };

    protected AbstractViewReferenceProvider(@Nonnull @DisplayExecutor final ListeningExecutorService displayExecutor,
                                            @Nonnull final DisplaySurfacePool displaySurfacePool) {
        this.displayExecutor = displayExecutor;
        this.displaySurfacePool = displaySurfacePool;
    }


    @Override
    public ListenableFuture<ViewReference> get() {
        return displayExecutor.submit(viewReferenceTask);
    }

    protected abstract void invokeViewBuild(ListenableFutureTask<Object> viewFuture);

    protected abstract Object createViewObject();

    protected abstract ViewReference createViewReference(Object createdViewObject, DisplaySurface displaySurface);

    protected abstract DisplaySurfaceHandle createDisplaySurfaceHandle(Object createdViewObject);
}
