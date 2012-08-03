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

import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.event.ButtonNotifyEvent;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.shell.core.api.ManagedDisplayService;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.input.api.ManagedMouse;
import org.trinity.shell.widget.api.DragButton;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

/**
 * A <code>DragButton</code> can move another
 * <code>GeoTransformableRectangle</cod>, defined by the target renderarea.
 * <p>
 * A <code>DragButton</code> monitors a <code>ManagedMouse</code> when the
 * <code>DragButton</code> is activated. Should the <code>ManagedMouse</code>
 * change it's position, the <code>DragButton</code> will move the target
 * <code>GeoTransformableRectangle</code> accordingly. A <code>DragButton</code>
 * will stop monitoring the <code>ManagedMouse</code> as soon as it is
 * deactivated.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class DragButtonImpl extends ButtonImpl implements DragButton {

	private final Runnable dragRun = new Runnable() {
		@Override
		public void run() {
			final Coordinate mousePos = DragButtonImpl.this.managedMouse
					.getAbsolutePosition();
			DragButtonImpl.this.x0 = mousePos.getX();
			DragButtonImpl.this.y0 = mousePos.getY();
			while (!Thread.interrupted()) {
				final Coordinate mousePosition = DragButtonImpl.this.managedMouse
						.getAbsolutePosition();
				final int x1 = mousePosition.getX();
				final int y1 = mousePosition.getY();

				final int deltaX = x1 - DragButtonImpl.this.x0;
				final int deltaY = y1 - DragButtonImpl.this.y0;

				if ((deltaX != 0) || (deltaY != 0)) {
					DragButtonImpl.this.x0 = x1;
					DragButtonImpl.this.y0 = y1;
					mutate(deltaX, deltaY);
				}

				// Process the next event without blocking, else we will never
				// receive the mouse button released event. This would result in
				// an endless loop where the target window would be "glued" to
				// the mouse cursor.
				DragButtonImpl.this.managedDisplay.postNextDisplayEvent(false);
			}
		}
	};

	private Thread dragThread = new Thread(this.dragRun);

	private GeoTransformableRectangle client;
	private int x0;
	private int y0;

	private final ManagedMouse managedMouse;
	private final ManagedDisplayService managedDisplay;

	private final DragButton.View view;

	/**
	 * 
	 */
	@Inject
	protected DragButtonImpl(	final EventBus eventBus,
								final GeoEventFactory geoEventFactory,
								final ManagedDisplayService managedDisplay,
								final PainterFactory painterFactory,
								@Named("Widget") final GeoExecutor geoExecutor,
								final ManagedMouse managedMouse,
								final DragButton.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
		this.managedMouse = managedMouse;
		this.managedDisplay = managedDisplay;
		stopDragClient();
		this.view = view;
	}

	/**
	 * Mutate the target <code>GeoTransformableRectangle</code> with the given
	 * delta values.
	 * <p>
	 * The given delta values indicate the movement of the
	 * <code>ManagedMouse</code>.
	 * 
	 * @param deltaX
	 *            The horizontal movement of the <code>ManagedMouse</code> in
	 *            pixels.
	 * @param deltaY
	 *            The vertical movement of the <code>ManagedMouse</code> in
	 *            pixels. @ Thrown when the target
	 *            <code>AbstractRenderArea</code> has an illegal state.
	 */
	protected void mutate(final int deltaX, final int deltaY) {
		getClient().setX(getClient().getX() + deltaX);
		getClient().setY(getClient().getY() + deltaY);
		getClient().requestMove();
	}

	/**
	 * Stop the <code>ManagedMouse</code> movement monitor for this
	 * <code>DragButton</code>.
	 */
	@Override
	public void stopDragClient() {
		this.dragThread.interrupt();
		// prepare for the next run
		this.dragThread = new Thread(this.dragRun);
		this.view.stopDrag(getClient());
	}

	@Subscribe
	public void onMouseButtonReleased(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STOPPED) {
			stopDragClient();
		} else {
			startDragClient();
		}
	}

	@Override
	public GeoTransformableRectangle getClient() {
		return this.client;
	}

	@Override
	public void setClient(final GeoTransformableRectangle client) {
		this.client = client;
	}

	@Override
	public void startDragClient() {
		if (!this.dragThread.isAlive()) {
			this.dragThread.start();
			this.view.startDrag(getClient());
		}
	}
}