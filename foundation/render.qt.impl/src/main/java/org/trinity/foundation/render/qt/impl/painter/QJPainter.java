/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.render.qt.impl.painter;

import java.util.concurrent.Callable;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.render.qt.impl.QJDisplaySurfaceHandle;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public class QJPainter implements Painter {

	private final Object model;
	private final Binder binder;
	private final ViewDiscovery viewDiscovery = new ViewDiscovery();
	private final DisplaySurfaceFactory displaySurfaceFactory;

	@AssistedInject
	QJPainter(	final DisplaySurfaceFactory displaySurfaceFactory,
				final Binder binder,
				@Assisted final Object model) {
		this.binder = binder;
		this.model = model;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public ListenableFuture<Void> destroy() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);

		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
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
				view.hide();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> grabKeyboard() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				view.grabKeyboard();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> grabPointer() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				view.grabMouse();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> ungrabPointer() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				view.releaseMouse();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> ungrabKeyboard() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() {
				view.releaseKeyboard();
				return null;
			}
		};

		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public void bindView() {
		final QWidget view = this.viewDiscovery.lookupView(this.model);
		this.binder.bind(	this.model,
							view);
	}

	@Override
	public ListenableFuture<DisplaySurface> getDislaySurface() {

		final QWidget view = this.viewDiscovery.lookupView(this.model);
		final Callable<DisplaySurface> callable = new Callable<DisplaySurface>() {
			@Override
			public DisplaySurface call() {
				final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(view);
				final DisplaySurface displaySurface = QJPainter.this.displaySurfaceFactory
						.createDisplaySurface(displaySurfaceHandle);
				return displaySurface;
			}
		};

		final ListenableFutureTask<DisplaySurface> futureTask = ListenableFutureTask.create(callable);
		QApplication.invokeLater(futureTask);
		return futureTask;
	}
}