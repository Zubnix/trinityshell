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

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ManagedDisplayService;
import org.trinity.shell.core.impl.AbstractRenderArea;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.widget.api.Widget;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
// TODO list emitted events
/**
 * An <code>AbstractRenderArea</code> with a <code>PaintableRenderNode</code>
 * implementation. A <code>Widget</code> is ment to provide a visual interface
 * for the user so it can manipulate the hyperdrive library at runtime.
 * <p>
 * A <code>Widget</code> is manipulated through a {@link Painter} that talks to
 * a paint back-end. This is done by the <code>Widget</code>'s {@link View}
 * object. The <code>View</code> object receives state change notifications of
 * the <code>Widget</code> and in turn returns a {@link PaintInstruction}. This
 * <code>PaintCall</code> is then fed to the <code>Widget</code>'s
 * <code>Painter</code>. A <code>View</code> is defined by the
 * {@link ViewClassFactory} implementation that was given as a parameter at
 * startup to the {@link ManagedDisplayService}.
 * <p>
 * A <code>Widget</code> is lazily initialized. This means that it is fully
 * initialized by the paint back-end when the <code>Widget</code> is assigned to
 * an already initialized parent <code>Widget</code>. A uninitialized
 * <code>Widget</code> will thus return null when <code>getManagedDisplay</code>, <code>getView</code>, <code>getPlatformRenderArea</code> is called. Calls
 * to <code>getPainter</code> will also fail.
 * <p>
 * An initialized <code>Widget</code> will have a {@link DisplayRenderArea}.
 * This is the native window that the <code>Widget</code> will be draw on. Note
 * that multiple <code>Widget</code>s can share the same
 * <code>PlatformRenderArea</code>. A <code>Widget</code> who's parent has a
 * different <code>PlatformRenderArea</code>, is called the owner of the
 * <code>PlatformRenderArea</code>. As a consequence the owning
 * <code>Widget</code>'s geometry should reflect the geometry of its
 * <code>PlatformRenderArea</code>.
 * <p>
 * Every <code>Widget</code> implements <code>HasGeoManager</code>. This means
 * that every <code>Widget</code> can optionally manage the geometry of it's
 * child <code>Widget</code>s. See the {@link org.hyperdrive.geo.impl} package
 * documentation for details.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */

// TODO use guice aop to send commands to the view
@Bind
public class WidgetImpl extends AbstractRenderArea implements Widget {

	private final Painter painter;
	private final GeoExecutor geoExecutor;
	private final ManagedDisplayService managedDisplay;
	private final EventBus eventBus;
	private final Widget.View view;

	/**
	 * Create an uninitialized <code>Widget</code>.A <code>Widget</code> will
	 * only be fully functional when it is assigned to an already initialized
	 * parent <code>Widget</code>.
	 */
	@Inject
	public WidgetImpl(	final EventBus eventBus,
						final GeoEventFactory geoEventFactory,
						final ManagedDisplayService managedDisplay,
						final PainterFactory painterFactory,
						@Named("Widget") final GeoExecutor geoExecutor,
						final Widget.View view) {
		super(eventBus, geoEventFactory, managedDisplay);
		this.managedDisplay = managedDisplay;
		this.eventBus = eventBus;
		this.geoExecutor = geoExecutor;
		this.painter = painterFactory.createPainter(this);
		this.view = view;
	}

	/**
	 * Initialize the <code>Widget</code> with the closest paintable parent
	 * <code>Widget</code>. The parent <code>Widget</code> can either be a
	 * direct or indirect parent of this <code>Widget</code>.
	 * 
	 * @param paintableParent
	 *            The <code>Widget</code>s closest parent <code>Widget</code>.
	 *            This is needed if a <code>Widget</code> needs to be directly
	 *            initialized with data from its paintable parent at the paint
	 *            back-end level.
	 */
	protected void init(final Widget paintableParent) {
		setPlatformRenderArea(this.view.create(this));
		this.managedDisplay.registerEventBusForSource(this.eventBus, this);
	}

	@Override
	public Painter getPainter() {
		if (this.painter == null) {
			throw new IllegalStateException("Widget not initialized.");
		}
		return this.painter;
	}

	@Override
	public GeoExecutor getGeoExecutor() {
		return this.geoExecutor;
	}

	@Override
	public Widget getParentPaintable() {
		return findParentPaintable(getParent());
	}

	/**
	 * Find the closest parent that is of type Widget.
	 * 
	 * @param square
	 * @return
	 */
	private Widget findParentPaintable(final GeoTransformableRectangle square) {
		if (square instanceof WidgetImpl) {
			return (Widget) square;
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
