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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hydrogen.api.display.PlatformRenderArea;
import org.hydrogen.api.display.ResourceHandle;
import org.hydrogen.api.display.input.KeyboardInput;
import org.hydrogen.api.display.input.MouseInput;
import org.hydrogen.api.paint.PaintCall;
import org.hydrogen.api.paint.Painter;
import org.hyperdrive.api.core.ManagedDisplay;
import org.hyperdrive.api.geo.GeoManager;
import org.hyperdrive.api.geo.GeoTransformableRectangle;
import org.hyperdrive.api.widget.ViewDefinition;
import org.hyperdrive.api.widget.Widget;
import org.hyperdrive.core.AbstractRenderArea;

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
 * {@link ViewClassFactory} implementation that was given as a parameter at
 * startup to the {@link ManagedDisplay}.
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
public class BaseWidget extends AbstractRenderArea implements Widget {

	private final View view = ViewBinder.createView(this,
			getViewDefinitionClass());

	private Painter painter;

	private final WidgetGeoExecutor geoExecutor;
	private GeoManager geoManager;

	/**
	 * Create an uninitialized <code>Widget</code>.A <code>Widget</code> will
	 * only be fully functional when it is assigned to an already initialized
	 * parent <code>Widget</code>.
	 * 
	 */
	public BaseWidget() {
		this.geoExecutor = new WidgetGeoExecutor(this);
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends View> getViewDefinitionClass() {
		// this code will probably not work when different classloaders are
		// used.

		final Class<? extends BaseWidget> widgetClass = getClass();
		final Class<?>[] declaredClasses = widgetClass.getClasses();

		Class<? extends View> viewClass = null;

		// find only the view interface defined by this widget, if none is
		// defined, fall back to the superclass view.
		for (final Class<?> declaredClass : declaredClasses) {
			if ((declaredClass.getAnnotation(ViewDefinition.class) != null)
					&& BaseWidget.View.class.isAssignableFrom(declaredClass)
					&& (widgetClass == declaredClass.getDeclaringClass())) {
				viewClass = (Class<? extends View>) declaredClass;
				break;
			} else if ((declaredClass.getAnnotation(ViewDefinition.class) != null)
					&& BaseWidget.View.class.isAssignableFrom(declaredClass)) {
				viewClass = (Class<? extends View>) declaredClass;
			}
		}

		if (viewClass == null) {
			// TODO log error. Paintable does not have a viewdefinition!
		}

		return viewClass;
	}

	@Override
	public View getView() {
		return this.view;
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
		// TODO
		// managedDisplay.registerEventBus(this, this);
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
	protected void init(final BaseWidget paintableParent) {

		// we use the paintableParent to determine the managed display.
		if (paintableParent != null) {
			setManagedDisplay(paintableParent.getManagedDisplay());
		}

		// we pass the paintable parent to the back-end which can choose to do
		// with it as it pleases.
		final Future<ResourceHandle> renderAreaId = getView().doCreate(this,
				isVisible(), paintableParent).getPaintResult();

		PlatformRenderArea renderArea = null;
		try {
			renderArea = getManagedDisplay()
					.getDisplay()
					.getDisplayPlatform()
					.findPaintablePlatformRenderAreaFromId(
							getManagedDisplay().getDisplay(),
							renderAreaId.get());
		} catch (final InterruptedException e) {
			// TODO log widget creation interrupt error.
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO log widget creation exception error.
			e.printStackTrace();
		}

		setPlatformRenderArea(renderArea);

		// TODO
		// getManagedDisplay().fireEvent(
		// new WidgetEvent<Widget>(WidgetEvent.WIDGET_INITIALIZED, this));

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
	public BaseWidget getParentPaintable() {
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
		// platformRenderArea.overrideRedirect(true);

		// platformRenderArea
		// .propagateEvent(EventPropagator.REDIRECT_CHILD_WINDOW_GEOMTRY_CHANGES);
		super.setPlatformRenderArea(platformRenderArea);
	}

	/**
	 * Find the closest parent that is of type Widget.
	 * 
	 * @param square
	 * @return
	 */
	private BaseWidget findParentPaintable(
			final GeoTransformableRectangle square) {
		if (square instanceof BaseWidget) {
			return (BaseWidget) square;
		} else {
			return findParentPaintable(square.getParent());
		}
	}

	@Override
	public void setInputFocus() {
		getPainter().setInputFocus();
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
	}

	@Override
	public void onMouseButtonReleased(final MouseInput input) {
	}

	@Override
	public void onKeyboardPressed(final KeyboardInput input) {
	}

	@Override
	public void onKeyboardReleased(final KeyboardInput input) {
	}

}
