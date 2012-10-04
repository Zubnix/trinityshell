/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.api.widget;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.text.View;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

// TODO split into abstract widget & move to api
/**
 * An <code>AbstractShellSurface</code> with a <code>PaintableSurfaceNode</code>
 * implementation. A <code>BaseShellWidget</code> is ment to provide a visual
 * interface for the user so it can manipulate the shell core library at
 * runtime.
 * <p>
 * A <code>BaseShellWidget</code> is manipulated through a {@link Painter} that
 * talks to a paint back-end. This is done by the <code>BaseShellWidget</code>'s
 * {@link View} object. The <code>View</code> object receives state change
 * notifications of the <code>BaseShellWidget</code> and in turn returns a
 * {@link PaintInstruction}. This <code>PaintCall</code> is then fed to the
 * <code>BaseShellWidget</code>'s <code>Painter</code>. A <code>View</code> is
 * defined by the {@link ViewClassFactory} implementation that was given as a
 * parameter at startup to the {@link ShellDisplay}.
 * <p>
 * A <code>BaseShellWidget</code> is lazily initialized. This means that it is
 * fully initialized by the paint back-end when the <code>BaseShellWidget</code>
 * is assigned to an already initialized parent <code>BaseShellWidget</code>. A
 * uninitialized <code>BaseShellWidget</code> will thus return null when
 * <code>getManagedDisplay</code>, <code>getView</code>,
 * <code>getPlatformRenderArea</code> is called. Calls to
 * <code>getPainter</code> will also fail.
 * <p>
 * An initialized <code>BaseShellWidget</code> will have a
 * {@link DisplaySurface} . This is the native window that the
 * <code>BaseShellWidget</code> will be draw on. Note that multiple
 * <code>BaseShellWidget</code>s can share the same
 * <code>PlatformRenderArea</code>. A <code>BaseShellWidget</code> who's parent
 * has a different <code>PlatformRenderArea</code>, is called the owner of the
 * <code>PlatformRenderArea</code>. As a consequence the owning
 * <code>BaseShellWidget</code>'s geometry should reflect the geometry of its
 * <code>PlatformRenderArea</code>.
 * <p>
 * Every <code>BaseShellWidget</code> implements <code>HasGeoManager</code>.
 * This means that every <code>BaseShellWidget</code> can optionally manage the
 * geometry of it's child <code>BaseShellWidget</code>s. See the
 * {@link org.hyperdrive.geo.impl} package documentation for details.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class BaseShellWidget extends AbstractShellSurfaceParent implements ShellWidget {

	private final Painter painter;
	private final BaseShellWidgetExecutor shellNodeExecutor;
	private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;
	private final EventBus eventBus;
	private final ShellWidgetView view;

	/**
	 * Create an uninitialized <code>BaseShellWidget</code>.A
	 * <code>BaseShellWidget</code> will only be fully functional when it is
	 * assigned to an already initialized parent <code>BaseShellWidget</code>.
	 */
	@Inject
	public BaseShellWidget(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							final ShellWidgetView view) {
		super(eventBus);
		this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		this.eventBus = eventBus;
		this.shellNodeExecutor = new BaseShellWidgetExecutor(this);
		this.painter = painterFactory.createPainter(this);
		this.view = view;
	}

	/**
	 * Initialize the <code>BaseShellWidget</code> with the closest paintable
	 * parent <code>BaseShellWidget</code>. The parent
	 * <code>BaseShellWidget</code> can either be a direct or indirect parent of
	 * this <code>BaseShellWidget</code>.
	 * 
	 * @param paintableParent
	 *            The <code>BaseShellWidget</code>s closest parent
	 *            <code>BaseShellWidget</code>. This is needed if a
	 *            <code>BaseShellWidget</code> needs to be directly initialized
	 *            with data from its paintable parent at the paint back-end
	 *            level.
	 */

	protected void init(final ShellSurface paintableParent) {
		this.shellDisplayEventDispatcher.registerDisplayEventSourceListener(this.eventBus,
																			this);
		this.view.createDisplaySurface(getPainter());
		this.shellDisplayEventDispatcher.registerDisplayEventSourceListener(this.eventBus,
																			getDisplaySurface());
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
		final Future<DisplaySurface> displaySurfaceFuture = this.view.getDislaySurface();
		if (displaySurfaceFuture == null) {
			return null;
		}
		DisplaySurface displaySurface = null;
		try {
			displaySurface = displaySurfaceFuture.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return displaySurface;
	}
}
