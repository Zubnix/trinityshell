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
package org.hyperdrive.widget.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.hydrogen.display.api.PlatformRenderArea;
import org.hydrogen.display.api.ResourceHandle;
import org.hydrogen.display.api.event.ButtonNotifyEvent;
import org.hydrogen.display.api.event.KeyNotifyEvent;
import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.display.api.input.MouseInput;
import org.hydrogen.paint.api.PaintInstruction;
import org.hydrogen.paint.api.Painter;
import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.foundation.api.event.KeyboardKeyPressedHandler;
import org.hyperdrive.foundation.api.event.KeyboardKeyReleasedHandler;
import org.hyperdrive.foundation.api.event.MouseButtonPressedHandler;
import org.hyperdrive.foundation.api.event.MouseButtonReleasedHandler;
import org.hyperdrive.foundation.impl.AbstractRenderArea;
import org.hyperdrive.geo.api.GeoManager;
import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.hyperdrive.widget.api.ViewBinder;
import org.hyperdrive.widget.api.ViewReference;
import org.hyperdrive.widget.api.Widget;

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
 * the <code>Widget</code> and in turn returns a {@link PaintInstruction}. This
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
 * child <code>Widget</code>s. See the {@link org.hyperdrive.geo.impl} package
 * documentation for details.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseWidget extends AbstractRenderArea implements Widget {

	@ViewReference
	private Widget.View view;

	private Painter painter;

	private final BaseWidgetGeoExecutor geoExecutor;
	private GeoManager geoManager;

	/**
	 * Create an uninitialized <code>Widget</code>.A <code>Widget</code> will
	 * only be fully functional when it is assigned to an already initialized
	 * parent <code>Widget</code>.
	 * 
	 */
	public BaseWidget() {
		ViewBinder.get().injectView(this);
		this.geoExecutor = new BaseWidgetGeoExecutor(this);
	}

	@Override
	public Widget.View getView() {
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
	}

	@Override
	protected void initEventHandlers() {
		super.initEventHandlers();

		addTypedEventHandler(new MouseButtonPressedHandler() {
			@Override
			public void handleEvent(final ButtonNotifyEvent event) {
				onMouseButtonPressed(event.getInput());
			}
		});
		addTypedEventHandler(new MouseButtonReleasedHandler() {
			@Override
			public void handleEvent(final ButtonNotifyEvent event) {
				onMouseButtonReleased(event.getInput());
			}
		});
		addTypedEventHandler(new KeyboardKeyPressedHandler() {

			@Override
			public void handleEvent(final KeyNotifyEvent event) {
				onKeyboardPressed(event.getInput());
			}
		});
		addTypedEventHandler(new KeyboardKeyReleasedHandler() {
			@Override
			public void handleEvent(final KeyNotifyEvent event) {
				onKeyboardReleased(event.getInput());
			}
		});
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
		this.painter = initPainter();

		// we pass the paintable parent to the back-end which can choose to do
		// with it as it pleases.
		final Future<ResourceHandle> futureResourceHandle = draw(getView()
				.doCreate(this, isVisible(), paintableParent));

		PlatformRenderArea renderArea = null;
		try {
			renderArea = getManagedDisplay()
					.getDisplay()
					.getDisplayPlatform()
					.findPaintablePlatformRenderAreaFromId(
							getManagedDisplay().getDisplay(),
							futureResourceHandle.get());
		} catch (final InterruptedException e) {
			// TODO log widget creation interrupt error.
			e.printStackTrace();
		} catch (final ExecutionException e) {
			// TODO log widget creation exception error.
			e.printStackTrace();
		}

		setPlatformRenderArea(renderArea);
		getManagedDisplay().addDisplayEventManager(this, this);
	}

	protected Painter initPainter() {
		return getManagedDisplay().getPainterFactory().getNewPainter(this);
	}

	@Override
	public Painter getPainter() {
		if (this.painter == null) {
			throw new IllegalStateException("Widget not initialized.");
		}
		return this.painter;
	}

	@Override
	public BaseWidgetGeoExecutor getGeoExecutor() {
		return this.geoExecutor;
	}

	@Override
	public BaseWidget getParentPaintable() {
		return findParentPaintable(getParent());
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
		// overrideable by subclasses
	}

	@Override
	public void onMouseButtonReleased(final MouseInput input) {
		// overrideable by subclasses
	}

	@Override
	public void onKeyboardPressed(final KeyboardInput input) {
		// overrideable by subclasses
	}

	@Override
	public void onKeyboardReleased(final KeyboardInput input) {
		// overrideable by subclasses
	}

	@Override
	protected void setPlatformRenderArea(
			final PlatformRenderArea platformRenderArea) {
		// repeated for package visibility
		super.setPlatformRenderArea(platformRenderArea);
	}

	protected <R> Future<R> draw(PaintInstruction<R, ?> paintCall) {
		return getPainter().paint(paintCall);
	}
}
