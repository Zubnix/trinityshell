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
package org.trinity.render.qt.impl.painter;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.qt.api.QJRenderEngine;
import org.trinity.render.qt.impl.painter.instructions.QJDestroyInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJGetDisplaySurfaceRoutine;
import org.trinity.render.qt.impl.painter.instructions.QJGiveFocusInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJGrabKeyboardInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJGrabPointer;
import org.trinity.render.qt.impl.painter.instructions.QJHideInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJLowerInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJMoveInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJMoveResizeInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJRaiseInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJResizeInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJSetParentInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJShowInstruction;
import org.trinity.render.qt.impl.painter.instructions.QJUngrabKeyboardRoutine;
import org.trinity.render.qt.impl.painter.instructions.QJUngrabPointer;
import org.trinity.shellplugin.widget.api.binding.BindingDiscovery;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.trolltech.qt.gui.QWidget;

public class QJPainter implements Painter {

	private final QJRenderEngine qFRenderEngine;
	private final PaintableSurfaceNode paintableSurfaceNode;
	private final BindingDiscovery bindingDiscovery;
	private final DisplaySurfaceFactory displaySurfaceFactory;
	private final QJViewBinder qjViewBinder;

	@Inject
	QJPainter(	final QJRenderEngine qFRenderEngine,
				DisplaySurfaceFactory displaySurfaceFactory,
				BindingDiscovery bindingDiscovery,
				QJViewBinder qjViewInitializier,
				@Assisted final PaintableSurfaceNode paintableSurfaceNode) {

		this.qFRenderEngine = qFRenderEngine;
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.displaySurfaceFactory = displaySurfaceFactory;
		this.bindingDiscovery = bindingDiscovery;
		this.qjViewBinder = qjViewInitializier;
	}

	@Override
	public void destroy() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJDestroyInstruction(view));
	}

	private QWidget getView() {
		return getView(paintableSurfaceNode);
	}

	private QWidget getView(PaintableSurfaceNode paintableSurfaceNode) {
		QWidget view = null;
		try {
			Optional<Method> viewRef = bindingDiscovery.lookupViewReference(paintableSurfaceNode.getClass());
			Object viewInst = viewRef.get().invoke(paintableSurfaceNode);
			checkArgument(viewInst instanceof QWidget);
			view = (QWidget) viewInst;
		} catch (IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (ExecutionException e) {
			Throwables.propagate(e);
		}

		return view;
	}

	@Override
	public void setInputFocus() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJGiveFocusInstruction(view));
	}

	@Override
	public void lower() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJLowerInstruction(view));
	}

	@Override
	public void show() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJShowInstruction(view));
	}

	@Override
	public void move(	final int x,
						final int y) {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJMoveInstruction(	view,
															x,
															y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJMoveResizeInstruction(view,
																x,
																y,
																width,
																height));
	}

	@Override
	public void raise() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJRaiseInstruction(view));
	}

	@Override
	public void setParent(	final DisplayArea parent,
							final int x,
							final int y) {
		checkArgument(parent instanceof PaintableSurfaceNode);
		QWidget parentView = getView((PaintableSurfaceNode) parent);
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJSetParentInstruction(	view,
																parentView,
																x,
																y));
	}

	@Override
	public void resize(	final int width,
						final int height) {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJResizeInstruction(view,
															width,
															height));
	}

	@Override
	public void hide() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJHideInstruction(view));
	}

	@Override
	public void grabKeyboard() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJGrabKeyboardInstruction(view));
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
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJGrabPointer(view));
	}

	@Override
	public void ungrabPointer() {
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJUngrabPointer(view));
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
		QWidget view = getView();
		this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
									new QJUngrabKeyboardRoutine(view));

	}

	@Override
	public void bindView(Optional<? extends PaintableSurfaceNode> parentPaintable) {

		QWidget parentView = null;
		if (parentPaintable.isPresent()) {
			parentView = getView(parentPaintable.get());
		}

		this.qjViewBinder.bindView(	Optional.fromNullable(parentView),
											getView(),
											paintableSurfaceNode);
	}

	@Override
	public Optional<DisplaySurface> getDislaySurface() {
		DisplaySurface displaySurface = null;

		QWidget view = getView();

		Future<DisplaySurface> dislaySurfaceFuture = this.qFRenderEngine
				.invoke(paintableSurfaceNode,
						new QJGetDisplaySurfaceRoutine(	displaySurfaceFactory,
														view));
		try {
			displaySurface = dislaySurfaceFuture.get();
		} catch (InterruptedException e) {
			Throwables.propagate(e);
		} catch (ExecutionException e) {
			Throwables.propagate(e);
		}

		return Optional.fromNullable(displaySurface);
	}
}