/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.ResourceHandle;
import org.hydrogen.displayinterface.event.DisplayEventSource;
import org.hydrogen.paintinterface.PaintCall;
import org.hydrogen.paintinterface.Paintable;
import org.hydrogen.paintinterface.Painter;
import org.hyperdrive.core.AbstractRenderArea;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.geo.GeoManager;
import org.hyperdrive.geo.GeoTransformableRectangle;
import org.hyperdrive.geo.HasGeoManager;

// TODO documentation
// TODO list emitted events
/**
 * An <code>AbstractRenderArea</code> with a <code>Paintable</code>
 * implementation. A <code>Widget</code> is ment to provide a visual interface
 * for the user so it can manipulate the hyperdrive library at runtime.
 * <p>
 * A <code>Widget</code> is manipulated through a {@link Painter} that talks to
 * a paint back-end. This is done by the <code>Widget</code>'s {@link View}
 * object. The <code>View</code> object receives state change notifications of
 * the <code>Widget</code> and in turn returns a {@link PaintCall}. This
 * <code>PaintCall</code> is then fed to the <code>Widget</code>'s
 * <code>Painter</code>. A <code>View</code> is defined by the
 * {@link ViewFactory} implementation that was given as a parameter at startup
 * to the {@link ManagedDisplay}.
 * <p>
 * A <code>Widget</code> is lazily initialized. This means that it is fully
 * initialized by the paint back-end when the <code>Widget</code> is assigned to
 * an already initialized parent <code>Widget</code>. A uninitialized
 * <code>Widget</code> will thus return null when <code>getManagedDisplay</code>, <code>getView</code>, <code>getPlatformRenderArea</code> is called. Calls
 * to <code>getPainter</code> will also fail.
 * <p>
 * An initialized <code>Widget</code> will have a {@link PlatformRenderArea}.
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
 * child <code>Widget</code>s. See the {@link org.hyperdrive.geo} package
 * documentation for details.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class Widget extends AbstractRenderArea implements Paintable,
		DisplayEventSource, HasGeoManager {

	private Painter painter;
	private View view;
	private final WidgetGeoExecutor geoExecutor;
	private GeoManager geoManager;

	/**
	 * Create an uninitialized <code>Widget</code>.A <code>Widget</code> will
	 * only be fully functional when it is assigned to an already initialized
	 * parent <code>Widget</code>.
	 * 
	 */
	public Widget() {
		this.geoExecutor = new WidgetGeoExecutor(this);
	}

	/**
	 * The (optional) <code>GeoManager</code> that will be consulted when a
	 * child <code>GeoTransformableRectangle</code> wishes to change its
	 * geometry.
	 * 
	 * @param geoManager
	 */
	public void setGeoManager(final GeoManager geoManager) {
		this.geoManager = geoManager;
	}

	@Override
	public GeoManager getGeoManager() {
		return this.geoManager;
	}

	@Override
	protected void setManagedDisplay(final ManagedDisplay managedDisplay) {
		super.setManagedDisplay(managedDisplay);
		managedDisplay.registerEventBus(this, this);
		this.view = initView(managedDisplay.getWidgetViewFactory());

	}

	/**
	 * Initialize the <code>Widget</code> with the closest parent
	 * <code>Widget</code>. The parent <code>Widget</code> can either be a
	 * direct or indirect parent of this <code>Widget</code>.
	 * 
	 * @param indirectParent
	 *            The <code>Widget</code>s closes parent <code>Widget</code>. @
	 */
	protected void init(final Widget indirectParent) {
		if (indirectParent != null) {
			setManagedDisplay(indirectParent.getManagedDisplay());

		}

		ResourceHandle renderAreaId;

		renderAreaId = getPainter().initPaintPeer(indirectParent,
				getPaintCallOnCreate());

		// if (renderAreaId <= 0) {
		// throw new HyperdriveException(new WidgetInitializationException(
		// this, String.format("Invalid render area id: %d",
		// renderAreaId)));
		// }
		PlatformRenderArea renderArea = null;

		renderArea = getManagedDisplay()
				.getDisplay()
				.getDisplayPlatform()
				.findPaintablePlatformRenderAreaFromId(
						getManagedDisplay().getDisplay(), renderAreaId);

		setPlatformRenderArea(renderArea);

		getManagedDisplay().fireEvent(
				new WidgetEvent<Widget>(WidgetEvent.WIDGET_INITIALIZED, this));

	}

	/**
	 * Construct a <code>View</code> object as defined by the given
	 * <code>ViewFactory</code>. The returned <code>View</code> determines this
	 * <code>Widget</code>s visual representation.
	 * 
	 * @param viewFactory
	 * @return
	 */
	protected View initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newBaseView();
	}

	/**
	 * Short for <code>getView().onCreate()</code>.
	 * 
	 * @return
	 */
	protected PaintCall<?, ?> getPaintCallOnCreate() {
		return getView().onCreate();
	}

	/**
	 * Short for <code>getView().onDestroy()</code>.
	 * 
	 * @return
	 */
	protected PaintCall<?, ?> getPaintCallOnDestroy() {
		return getView().onDestroy();
	}

	@Override
	public Painter getPainter() {
		// We can not initialize the painter when constructing the widget
		// because the managed display is not yet set. Instead we lazily
		// initialize the painter. This means this method will fail if the
		// widget is not yet initialized, ie the widget requires a managed
		// display.

		// TODO move painter initialization to init method
		if (this.painter == null) {
			this.painter = getManagedDisplay().getPainterFactory()
					.getNewPainter(this);
		}
		return this.painter;
	}

	@Override
	public WidgetGeoExecutor getGeoExecutor() {
		return this.geoExecutor;
	}

	@Override
	public Widget getParentPaintable() {
		return findParentPaintable(getParent());
	}

	@Override
	protected void setPlatformRenderArea(
			final PlatformRenderArea platformRenderArea) {
		// TODO this is more an X specific thing, create a more platform
		// neutral mechanism/interface...

		// override redirect is enabled so the widget bypasses any event
		// creation on the native display. This is needed so that the widget
		// is not mistaken as a client window. This should normally be done
		// by the paint-back when creating the widget 'visual'. Enabling it
		// here is thus more some kind of a safety precaution.
		platformRenderArea.overrideRedirect(true);
		super.setPlatformRenderArea(platformRenderArea);
	}

	/**
	 * Find the closest parent that is of type Widget.
	 * 
	 * @param square
	 * @return
	 */
	private Widget findParentPaintable(final GeoTransformableRectangle square) {
		if (square instanceof Widget) {
			return (Widget) square;
		} else {
			return findParentPaintable(square.getParent());
		}
	}

	/**
	 * 
	 * @return
	 */
	public View getView() {
		return this.view;
	}

	@Override
	public void giveInputFocus() {
		getPainter().setInputFocus();
	}
}
