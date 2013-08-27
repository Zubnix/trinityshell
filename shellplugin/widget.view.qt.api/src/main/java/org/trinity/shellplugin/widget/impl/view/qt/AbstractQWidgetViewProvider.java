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

import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Provider;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractQWidgetViewProvider implements Provider<Object> {

	private final ListeningExecutorService displayExecutor;
	private final DisplaySurfacePool displaySurfacePool;

	protected AbstractQWidgetViewProvider(	@DisplayExecutor final ListeningExecutorService displayExecutor,
											final DisplaySurfacePool displaySurfacePool) {
		this.displayExecutor = displayExecutor;
		this.displaySurfacePool = displaySurfacePool;
	}

	@Override
	public Object get() {

		final ListenableFutureTask<QWidget> viewCreationTask = create(new Callable<QWidget>() {
			@Override
			public QWidget call() {
				return createView();
			}
		});

		return displayExecutor.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				try (DisplaySurfaceCreator displaySurfaceCreator = displaySurfacePool.getDisplaySurfaceCreator()) {
					QApplication.invokeLater(viewCreationTask);
					displaySurfaceCreator.create(new ViewDisplaySurfaceHandle(viewCreationTask.get()));
				}
				return viewCreationTask.get();
			}
		});
	}

	protected abstract QWidget createView();
}
