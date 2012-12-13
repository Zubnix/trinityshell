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
import org.trinity.render.qt.impl.painter.instructions.QJCreateSurfaceRoutine;
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
import org.trinity.shellplugin.widget.api.binding.ViewPropertyDiscovery;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.trolltech.qt.gui.QWidget;

public class QJPainter implements Painter {

	private final QJRenderEngine qFRenderEngine;
	private final PaintableSurfaceNode paintableSurfaceNode;
	private final ViewPropertyDiscovery viewPropertyDiscovery;
	private final ViewSlotInvocationHandler viewSlotInvocationHandler;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	QJPainter(	final QJRenderEngine qFRenderEngine,
				ViewPropertyDiscovery viewPropertyDiscovery,
				ViewSlotInvocationHandler viewSlotInvocationHandler,
				DisplaySurfaceFactory displaySurfaceFactory,
				@Assisted final PaintableSurfaceNode paintableSurfaceNode) {

		this.qFRenderEngine = qFRenderEngine;
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.viewPropertyDiscovery = viewPropertyDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public void destroy() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJDestroyInstruction(view.get()));
		}
	}

	private Optional<QWidget> getView() {
		return getView(paintableSurfaceNode);
	}

	private Optional<QWidget> getView(PaintableSurfaceNode paintableSurfaceNode) {
		QWidget view = null;
		try {
			Optional<Method> viewRef = viewPropertyDiscovery.lookupViewReference(paintableSurfaceNode.getClass());
			if (viewRef.isPresent()) {
				Object viewInst = viewRef.get().invoke(paintableSurfaceNode);
				checkArgument(viewInst instanceof QWidget);
				view = (QWidget) viewInst;
			}
		} catch (IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (InvocationTargetException e) {
			Throwables.propagate(e);
		} catch (ExecutionException e) {
			Throwables.propagate(e);
		}

		return Optional.fromNullable(view);
	}

	@Override
	public void setInputFocus() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJGiveFocusInstruction(view.get()));
		}
	}

	@Override
	public void lower() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJLowerInstruction(view.get()));
		}
	}

	@Override
	public void show() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJShowInstruction(view.get()));
		}
	}

	@Override
	public void move(	final int x,
						final int y) {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJMoveInstruction(	view.get(),
																x,
																y));
		}
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJMoveResizeInstruction(view.get(),
																	x,
																	y,
																	width,
																	height));
		}
	}

	@Override
	public void raise() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJRaiseInstruction(view.get()));
		}
	}

	@Override
	public void setParent(	final DisplayArea parent,
							final int x,
							final int y) {
		checkArgument(parent instanceof PaintableSurfaceNode);
		Optional<QWidget> parentView = getView((PaintableSurfaceNode) parent);
		Optional<QWidget> view = getView();
		if (view.isPresent() && parentView.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJSetParentInstruction(	view.get(),
																	parentView.get(),
																	x,
																	y));
		}
	}

	@Override
	public void resize(	final int width,
						final int height) {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJResizeInstruction(view.get(),
																width,
																height));
		}
	}

	@Override
	public void hide() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJHideInstruction(view.get()));
		}
	}

	@Override
	public void grabKeyboard() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJGrabKeyboardInstruction(view.get()));
		}
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
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJGrabPointer(view.get()));
		}
	}

	@Override
	public void ungrabPointer() {
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJUngrabPointer(view.get()));
		}

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
		Optional<QWidget> view = getView();
		if (view.isPresent()) {
			this.qFRenderEngine.invoke(	this.paintableSurfaceNode,
										new QJUngrabKeyboardRoutine(view.get()));
		}

	}

	@Override
	public void initView(Optional<? extends PaintableSurfaceNode> closestParentPaintable) {

		QWidget parentView = null;
		if (closestParentPaintable.isPresent()) {
			parentView = getView(closestParentPaintable.get()).get();
		}

		this.qFRenderEngine.invoke(	paintableSurfaceNode,
									new QJCreateSurfaceRoutine(	viewPropertyDiscovery,
																viewSlotInvocationHandler,
																Optional.fromNullable(parentView),
																getView().get()));
	}

	@Override
	public Optional<DisplaySurface> getDislaySurface() {
		DisplaySurface displaySurface = null;

		Optional<QWidget> view = getView();

		if (view.isPresent()) {
			Future<DisplaySurface> dislaySurfaceFuture = this.qFRenderEngine
					.invoke(paintableSurfaceNode,
							new QJGetDisplaySurfaceRoutine(	displaySurfaceFactory,
															view.get()));
			try {
				displaySurface = dislaySurfaceFuture.get();
			} catch (InterruptedException e) {
				Throwables.propagate(e);
			} catch (ExecutionException e) {
				Throwables.propagate(e);
			}
		}

		return Optional.fromNullable(displaySurface);
	}
}