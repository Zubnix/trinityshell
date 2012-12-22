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
package org.trinity.shell.api.widget;

import javax.swing.text.View;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * An {@link AbstractShellSurfaceParent} with a base
 * {@link PaintableSurfaceNode} implementation.
 * <p>
 * A <code>BaseShellWidget</code> is manipulated through a {@link Painter} that
 * can talk to a paint back-end. This is done by an enclosed {@link View}
 * object. The <code>View</code> object receives state change notifications of
 * the <code>BaseShellWidget</code> and in turn talks to the {@code Painter} of
 * the <code>BaseShellWidget</code>.
 * <p>
 * A <code>BaseShellWidget</code> is lazily initialized. This means that it is
 * fully initialized by the paint back-end when the <code>BaseShellWidget</code>
 * is assigned to an already initialized parent <code>BaseShellWidget</code>.
 * <p>
 * An initialized <code>BaseShellWidget</code> will have a
 * {@link DisplaySurface} . This surface maps to the native window that the
 * <code>BaseShellWidget</code> will be draw on. Note that multiple
 * <code>BaseShellWidget</code>s can share the same <code>DisplaySurface</code>.
 */
@Bind
public class BaseShellWidget extends AbstractShellSurfaceParent implements ShellWidget {

	private class DestroyCallback {
		@Subscribe
		public void handleDestroy(final ShellNodeDestroyedEvent destroyEvent) {
			BaseShellWidget.this.shellDisplayEventDispatcher
					.unregisterAllDisplayEventSourceListeners(getDisplaySurface());
			BaseShellWidget.this.shellDisplayEventDispatcher
					.unregisterAllDisplayEventSourceListeners(BaseShellWidget.this);
		}
	}

	private final Painter painter;
	private final BaseShellWidgetExecutor shellNodeExecutor;
	private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;
	private final EventBus eventBus;

	/**
	 * Create an uninitialized <code>BaseShellWidget</code>.A
	 * <code>BaseShellWidget</code> will only be fully functional when it is
	 * assigned to an already initialized parent <code>BaseShellWidget</code>.
	 */
	@Inject
	public BaseShellWidget(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory) {
		super(eventBus);
		this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		this.eventBus = eventBus;
		this.shellNodeExecutor = new BaseShellWidgetExecutor(this);
		this.painter = painterFactory.createPainter(this);
	}

	protected void init(final BaseShellWidget closestParentWidget) {
		this.shellDisplayEventDispatcher.registerDisplayEventSourceListener(this.eventBus,
																			this);
		this.painter.bindView(Optional.fromNullable(closestParentWidget));
		this.shellDisplayEventDispatcher.registerDisplayEventSourceListener(this.eventBus,
																			getDisplaySurface());
		addShellNodeEventHandler(new DestroyCallback());
	}

	@Override
	public Painter getPainter() {
		return this.painter;
	}

	@Override
	public BaseShellWidgetExecutor getShellNodeExecutor() {
		return this.shellNodeExecutor;
	}

	@Override
	public BaseShellWidget getParentPaintableSurface() {
		return findParentPaintable(getParent());
	}

	/**
	 * Find the closest parent that is of type BaseShellWidget.
	 * 
	 * @param square
	 * @return
	 */
	private BaseShellWidget findParentPaintable(final ShellNode square) {
		if (square == null) {
			return null;
		}
		if (square instanceof BaseShellWidget) {
			return (BaseShellWidget) square;
		}

		if (square.getParent().equals(square)) {
			return null;
		}
		return findParentPaintable(square.getParent());

	}

	@Override
	public void setInputFocus() {

		getPainter().setInputFocus();
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		DisplaySurface displaySurface = this.painter.getDislaySurface().get();
		return displaySurface;
	}
}
