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

package org.trinity.shellplugin.widget.impl.view.qt;

import static com.google.common.util.concurrent.ListenableFutureTask.create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.render.ViewReference;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Provider;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractQWidgetViewReferenceProvider implements Provider<ViewReference> {

	private final ListeningExecutorService displayExecutor;
	private final DisplaySurfacePool displaySurfacePool;

	protected AbstractQWidgetViewReferenceProvider(@DisplayExecutor final ListeningExecutorService displayExecutor,
                                                   final DisplaySurfacePool displaySurfacePool) {
		this.displayExecutor = displayExecutor;
		this.displaySurfacePool = displaySurfacePool;
	}

	@Override
	public ViewReference get() {

		final ListenableFutureTask<Object> viewFuture = create(new Callable<Object>() {
			@Override
			public Object call() {
				return createViewCall();
			}
		});

        final ListenableFuture<DisplaySurface> displaySurfaceFuture = displayExecutor.submit(new Callable<DisplaySurface>() {
            @Override
            public DisplaySurface call() throws ExecutionException, InterruptedException {
                try (DisplaySurfaceCreator displaySurfaceCreator = displaySurfacePool.getDisplaySurfaceCreator()) {
                    QApplication.invokeLater(viewFuture);
                    final Object qWidget = viewFuture.get();
                    return displaySurfaceCreator.create(new ViewDisplaySurfaceHandle((QWidget) qWidget));
                }
            }
        });

        return new ViewReference() {
            @Override
            public ListenableFuture<Object> getView() {
                return viewFuture;
            }

            @Override
            public ListenableFuture<DisplaySurface> getViewDisplaySurface() {
                return displaySurfaceFuture;
            }
        };
    }

	protected abstract Object createViewCall();
}
