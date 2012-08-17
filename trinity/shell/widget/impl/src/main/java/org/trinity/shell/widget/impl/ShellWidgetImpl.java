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
package org.trinity.shell.widget.impl;

import javax.swing.text.View;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.impl.AbstractShellRenderArea;
import org.trinity.shell.geo.api.ShellGeoExecutor;
import org.trinity.shell.geo.api.ShellGeoNode;
import org.trinity.shell.widget.api.ShellWidget;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
// TODO list emitted events
/**
 * An <code>AbstractShellRenderArea</code> with a
 * <code>PaintableRenderNode</code> implementation. A <code>ShellWidget</code>
 * is ment to provide a visual interface for the user so it can manipulate the
 * hyperdrive library at runtime.
 * <p>
 * A <code>ShellWidget</code> is manipulated through a {@link Painter} that
 * talks to a paint back-end. This is done by the <code>ShellWidget</code>'s
 * {@link View} object. The <code>View</code> object receives state change
 * notifications of the <code>ShellWidget</code> and in turn returns a
 * {@link PaintInstruction}. This <code>PaintCall</code> is then fed to the
 * <code>ShellWidget</code>'s <code>Painter</code>. A <code>View</code> is
 * defined by the {@link ViewClassFactory} implementation that was given as a
 * parameter at startup to the {@link ShellDisplay}.
 * <p>
 * A <code>ShellWidget</code> is lazily initialized. This means that it is fully
 * initialized by the paint back-end when the <code>ShellWidget</code> is
 * assigned to an already initialized parent <code>ShellWidget</code>. A
 * uninitialized <code>ShellWidget</code> will thus return null when
 * <code>getManagedDisplay</code>, <code>getView</code>,
 * <code>getPlatformRenderArea</code> is called. Calls to
 * <code>getPainter</code> will also fail.
 * <p>
 * An initialized <code>ShellWidget</code> will have a {@link DisplayRenderArea}
 * . This is the native window that the <code>ShellWidget</code> will be draw
 * on. Note that multiple <code>ShellWidget</code>s can share the same
 * <code>PlatformRenderArea</code>. A <code>ShellWidget</code> who's parent has
 * a different <code>PlatformRenderArea</code>, is called the owner of the
 * <code>PlatformRenderArea</code>. As a consequence the owning
 * <code>ShellWidget</code>'s geometry should reflect the geometry of its
 * <code>PlatformRenderArea</code>.
 * <p>
 * Every <code>ShellWidget</code> implements <code>HasGeoManager</code>. This
 * means that every <code>ShellWidget</code> can optionally manage the geometry
 * of it's child <code>ShellWidget</code>s. See the
 * {@link org.hyperdrive.geo.impl} package documentation for details.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellWidgetImpl extends AbstractShellRenderArea implements
		ShellWidget {

	private final Painter painter;
	private final ShellGeoExecutor shellGeoExecutor;
	private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;
	private final EventBus eventBus;
	private final ShellWidgetView view;

	/**
	 * Create an uninitialized <code>ShellWidget</code>.A
	 * <code>ShellWidget</code> will only be fully functional when it is
	 * assigned to an already initialized parent <code>ShellWidget</code>.
	 */
	@Inject
	public ShellWidgetImpl(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							@Named("ShellWidget") final ShellGeoExecutor shellGeoExecutor,
							final ShellWidgetView view) {
		super(eventBus, shellDisplayEventDispatcher);
		this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		this.eventBus = eventBus;
		this.shellGeoExecutor = shellGeoExecutor;
		this.painter = painterFactory.createPainter(this);
		this.view = view;
	}

	/**
	 * Initialize the <code>ShellWidget</code> with the closest paintable parent
	 * <code>ShellWidget</code>. The parent <code>ShellWidget</code> can either
	 * be a direct or indirect parent of this <code>ShellWidget</code>.
	 * 
	 * @param paintableParent
	 *            The <code>ShellWidget</code>s closest parent
	 *            <code>ShellWidget</code>. This is needed if a
	 *            <code>ShellWidget</code> needs to be directly initialized with
	 *            data from its paintable parent at the paint back-end level.
	 */
	protected void init(final ShellWidget paintableParent) {
		setPlatformRenderArea(this.view.create(this));
		this.shellDisplayEventDispatcher
				.registerDisplayEventSource(this.eventBus, this);
	}

	@Override
	public Painter getPainter() {
		if (this.painter == null) {
			throw new IllegalStateException("ShellWidget not initialized.");
		}
		return this.painter;
	}

	@Override
	public ShellGeoExecutor getGeoExecutor() {
		return this.shellGeoExecutor;
	}

	@Override
	public ShellWidget getParentPaintable() {
		return findParentPaintable(getParent());
	}

	/**
	 * Find the closest parent that is of type ShellWidget.
	 * 
	 * @param square
	 * @return
	 */
	private ShellWidget findParentPaintable(final ShellGeoNode square) {
		if (square instanceof ShellWidgetImpl) {
			return (ShellWidget) square;
		} else {
			return findParentPaintable(square.getParent());
		}
	}

	@Override
	public void setInputFocus() {
		getPainter().setInputFocus();
	}

	@Override
	protected void setPlatformRenderArea(final DisplayRenderArea platformRenderArea) {
		// repeated for package visibility
		super.setPlatformRenderArea(platformRenderArea);
	}
}
