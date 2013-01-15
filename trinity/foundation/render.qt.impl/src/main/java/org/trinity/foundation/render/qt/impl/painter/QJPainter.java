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

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.Painter;
import org.trinity.foundation.api.render.Renderer;
import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.render.qt.impl.painter.routine.QJGetDisplaySurfaceRoutine;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.trolltech.qt.gui.QWidget;

public class QJPainter implements Painter {

	private final Renderer renderEngine;
	private final Object dataContext;
	private final BindingDiscovery bindingDiscovery;
	private final DisplaySurfaceFactory displaySurfaceFactory;
	private final QJViewBinder qjViewBinder;

	@Inject
	QJPainter(	final Renderer qFRenderEngine,
				final DisplaySurfaceFactory displaySurfaceFactory,
				final BindingDiscovery bindingDiscovery,
				final QJViewBinder qjViewInitializier,
				@Assisted final Object dataContext) {

		this.renderEngine = qFRenderEngine;
		this.dataContext = dataContext;
		this.displaySurfaceFactory = displaySurfaceFactory;
		this.bindingDiscovery = bindingDiscovery;
		this.qjViewBinder = qjViewInitializier;
	}

	@Override
	public void destroy() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.close();
											return null;
										}
									});
	}

	private QWidget getView() {
		return getView(this.dataContext);
	}

	private QWidget getView(final Object dataContext) {
		QWidget view = null;
		try {
			final Optional<Method> viewRef = this.bindingDiscovery.lookupView(dataContext.getClass());
			final Object viewInst = viewRef.get().invoke(dataContext);
			checkArgument(viewInst instanceof QWidget);
			view = (QWidget) viewInst;
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}

		return view;
	}

	@Override
	public void setInputFocus() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.setFocus();
											return null;
										}
									});
	}

	@Override
	public void lower() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.lower();
											return null;
										}
									});
	}

	@Override
	public void show() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.show();
											return null;
										}
									});
	}

	@Override
	public void move(	final int x,
						final int y) {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.move(	x,
														y);
											return null;
										}
									});
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.setGeometry(	x,
																y,
																width,
																height);
											return null;
										}
									});
	}

	@Override
	public void raise() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.raise();
											return null;
										}
									});
	}

	@Override
	public void setParent(	final DisplayArea parent,
							final int x,
							final int y) {
		final QWidget parentView = getView(parent);
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.setParent(parentView);
											view.move(	x,
														y);
											return null;
										}
									});
	}

	@Override
	public void resize(	final int width,
						final int height) {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.resize(width,
														height);
											return null;
										}
									});
	}

	@Override
	public void hide() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.hide();
											return null;
										}
									});
	}

	@Override
	public void grabKeyboard() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.grabKeyboard();
											return null;
										}
									});
	}

	@Override
	public void grabButton(	final Button grabButton,
							final InputModifiers withModifiers) {
		// this.qFRenderEngine.invoke( this.paintableSurfaceNode,
		// new PaintRoutine<Void, QJPaintContext>() {
		// @Override
		// public Void call(final QJPaintContext paintContext) {
		// // ???
		// return null;
		// }
		// });
	}

	@Override
	public void grabPointer() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.grabMouse();
											return null;
										}
									});
	}

	@Override
	public void ungrabPointer() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.releaseMouse();
											return null;
										}
									});
	}

	@Override
	public void ungrabButton(	final Button ungrabButton,
								final InputModifiers withModifiers) {
		// this.qFRenderEngine.invoke( this.paintableSurfaceNode,
		// new PaintRoutine<Void, QJPaintContext>() {
		// @Override
		// public Void call(final QJPaintContext paintContext) {
		// // ???
		// return null;
		// }
		// });

	}

	@Override
	public void grabKey(final Key grabKey,
						final InputModifiers withModifiers) {
		// this.qFRenderEngine.invoke( this.paintableSurfaceNode,
		// new PaintRoutine<Void, QJPaintContext>() {
		// @Override
		// public Void call(final QJPaintContext paintContext) {
		// // ???
		// return null;
		// }
		// });
	}

	@Override
	public void ungrabKey(	final Key ungrabKey,
							final InputModifiers withModifiers) {
		// this.qFRenderEngine.invoke( this.paintableSurfaceNode,
		// new PaintRoutine<Void, QJPaintContext>() {
		// @Override
		// public Void call(final QJPaintContext paintContext) {
		// // ???
		// return null;
		// }
		// });

	}

	@Override
	public void ungrabKeyboard() {
		final QWidget view = getView();
		this.renderEngine.invoke(	this.dataContext,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) {
											view.releaseKeyboard();
											return null;
										}
									});

	}

	@Override
	public void bindView(final Optional<?> parentDataContext) {

		QWidget parentView = null;
		if (parentDataContext.isPresent()) {
			parentView = getView(parentDataContext.get());
		}

		this.qjViewBinder.bindView(	Optional.fromNullable(parentView),
									getView(),
									this.dataContext);
	}

	@Override
	public Optional<DisplaySurface> getDislaySurface() {
		DisplaySurface displaySurface = null;

		final QWidget view = getView();

		final Future<DisplaySurface> dislaySurfaceFuture = this.renderEngine
				.invoke(this.dataContext,
						new QJGetDisplaySurfaceRoutine(	this.displaySurfaceFactory,
														view));
		try {
			displaySurface = dislaySurfaceFuture.get();
		} catch (final InterruptedException e) {
			Throwables.propagate(e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}

		return Optional.fromNullable(displaySurface);
	}
}