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

import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.DisplaySurfacePreparation;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Provider;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractQWidgetViewProvider implements Provider<ListenableFuture<Object>> {

	private final DisplaySurfacePool displaySurfacePool;
	private final DisplaySurfacePreparation displaySurfacePreparation;

	protected AbstractQWidgetViewProvider(	final DisplaySurfacePool displaySurfacePool,
											DisplaySurfacePreparation displaySurfacePreparation) {
		this.displaySurfacePool = displaySurfacePool;
		this.displaySurfacePreparation = displaySurfacePreparation;
	}

	@Override
	public ListenableFuture<Object> get() {
			try(DisplaySurfacePreparation displaySurfacePreparation = this.displaySurfacePreparation){
				displaySurfacePreparation.begin();
				return prepareView();
			}
	}

	protected ListenableFutureTask<Object> prepareView() {
		final ListenableFutureTask<Object> futureTask = create(new Callable<Object>() {
			@Override
			public Object call() {

				final QWidget view = createView();
				// this will register it in the pool
				displaySurfacePool.getDisplaySurface(new ViewDisplaySurfaceHandle(view));
				return view;
			}
		});
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	protected abstract QWidget createView();
}
