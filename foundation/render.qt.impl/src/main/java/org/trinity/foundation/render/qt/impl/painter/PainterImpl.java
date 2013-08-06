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
package org.trinity.foundation.render.qt.impl.painter;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.render.qt.impl.RenderDisplaySurfaceHandle;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

@Deprecated
@ThreadSafe
public class PainterImpl implements Painter {

	private static final Logger LOG = LoggerFactory.getLogger(PainterImpl.class);
	private final ListeningExecutorService modelExecutor;
	private final AsyncListenable model;
	private final Binder binder;
	private final ViewDiscovery viewDiscovery = new ViewDiscovery();
	private final DisplaySurfaceFactory displaySurfaceFactory;
	private final ViewEventTrackerFactory eventTrackerFactory;

	@AssistedInject
	PainterImpl(final DisplaySurfaceFactory displaySurfaceFactory,
				final ViewEventTrackerFactory eventTrackerFactory,
				final Binder binder,
				@Assisted final ListeningExecutorService modelExecutor,
				@Assisted final AsyncListenable model) {
		this.binder = binder;
		this.modelExecutor = modelExecutor;
		this.model = model;
		this.displaySurfaceFactory = displaySurfaceFactory;
		this.eventTrackerFactory = eventTrackerFactory;
	}

	@Override
	public ListenableFuture<Void> destroy() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);

		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] close.",
							view);
				view.close();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> setInputFocus() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);

		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] set input focus.",
							view);
				view.setFocus();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> lower() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] lower.",
							view);
				view.lower();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> show() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] show.",
							view);
				view.show();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> move(	final int x,
										final int y) {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] move x={}, y={}.",
							view,
							x,
							y);
				view.move(	x,
							y);
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> moveResize(	final int x,
												final int y,
												final int width,
												final int height) {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] move resize x={}, y={}, width={}, height={}.",
							view,
							x,
							y,
							width,
							height);
				view.setGeometry(	x,
									y,
									width,
									height);
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> raise() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] raise.",
							view);
				view.raise();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> setParent(final DisplayArea parent,
											final int x,
											final int y) {
		final QWidget parentView = this.viewDiscovery.lookupView(parent);
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] set parent parentView={}, x={}, y={}.",
							view,
							parentView,
							x,
							y);
				view.setParent(parentView);
				view.move(	x,
							y);
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> resize(	final int width,
											final int height) {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] resize width={}, height={}.",
							view,
							width,
							height);
				view.resize(width,
							height);
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> hide() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				LOG.debug(	"[view={}] hide.",
							view);
				view.hide();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> bindView() {

		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Runnable viewBindingRoutine = new Runnable() {
			@Override
			public void run() {
				final QObject eventTracker = PainterImpl.this.eventTrackerFactory
						.createQJEventTracker(	PainterImpl.this.model,
												view);
				view.installEventFilter(eventTracker);
				PainterImpl.this.binder.bind(	PainterImpl.this.modelExecutor,
												PainterImpl.this.model,
												view);

			}
		};
		final ListenableFutureTask<Void> bindViewFuture = ListenableFutureTask.create(	viewBindingRoutine,
																						null);
		QApplication.invokeLater(viewBindingRoutine);
		return bindViewFuture;
	}

	@Override
	public ListenableFuture<DisplaySurface> getDislaySurface() {

		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<DisplaySurface> callable = new Callable<DisplaySurface>() {
			@Override
			public DisplaySurface call() {
				final DisplaySurfaceHandle displaySurfaceHandle = new RenderDisplaySurfaceHandle(view);
				return PainterImpl.this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);
			}
		};

		final ListenableFutureTask<DisplaySurface> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}
}
