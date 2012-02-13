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
package org.hyperdrive.core;

import org.hydrogen.displayinterface.EventPropagator;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.event.ConfigureRequestEvent;
import org.hydrogen.displayinterface.event.MapRequestEvent;
import org.hydrogen.displayinterface.event.UnmappedNotifyEvent;
import org.hydrogen.eventsystem.EventHandler;

// TODO documentation
/**
 * A <code>ClientWindow</code> wraps a {@link PlatformRenderArea} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ClientWindow</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ClientWindow</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class ClientWindow extends AbstractRenderArea {

	private final RenderAreaGeoExecutor renderAreaGeoExecutor;

	/**
	 * Create a new <code>ClientWindow</code> from a foreign
	 * <code>PlatformRenderArea</code>.
	 * 
	 * @param managedDisplay
	 *            The {@link ManagedDisplay} where this
	 *            <code>ClientWindow</code> lives on.
	 * @param clientWindow
	 *            The foreign {@link PlatformRenderArea}.
	 * 
	 */
	public ClientWindow(final ManagedDisplay managedDisplay,
			final PlatformRenderArea clientWindow) {
		super(managedDisplay, clientWindow);

		getManagedDisplay().registerEventBus(clientWindow, this);

		this.renderAreaGeoExecutor = new RenderAreaGeoExecutor(this);
		// TODO real root or fake root?
		setParent(managedDisplay.getRealRootRenderArea());
		this.doUpdateParentValue(false);
		initEventHandlers();

		getManagedDisplay().fireEvent(
				new ClientEvent(ClientEvent.CLIENT_INITIALIZED, this));

	}

	/**
	 * {@inheritDoc}
	 * <ul>
	 * <li>MapRequestEvent.TYPE</li>
	 * <li>ConfigureRequestEvent.TYPE</li>
	 * <li>UnmappedNotifyEvent.TYPE</li>
	 * </ul>
	 */
	@Override
	protected void initEventHandlers() {
		// move all event handles to super class render area?
		super.initEventHandlers();
		// Display event handlers

		this.addEventHandler(new EventHandler<MapRequestEvent>() {
			@Override
			public void handleEvent(final MapRequestEvent event) {

				ClientWindow.this.handleMapRequest(event);

			}
		}, MapRequestEvent.TYPE);
		this.addEventHandler(new EventHandler<ConfigureRequestEvent>() {
			@Override
			public void handleEvent(final ConfigureRequestEvent event) {

				ClientWindow.this.handleConfigureRequest(event);

			}
		}, ConfigureRequestEvent.TYPE);

		this.addEventHandler(new EventHandler<UnmappedNotifyEvent>() {
			@Override
			public void handleEvent(final UnmappedNotifyEvent event) {
				ClientWindow.this.handleUnmapNotify(event);
			}
		}, UnmappedNotifyEvent.TYPE);
	}

	// TODO implement through input interface? (set on abstractrenderarea &
	// also implement in widget?)
	/**
	 * 
	 * @return
	 * 
	 */
	public boolean hasInputFocus() {
		return getManagedDisplay().getDisplay().getInputFocus()
				.getDisplayResourceHandle().getResourceHandle() == getPlatformRenderArea()
				.getDisplayResourceHandle().getResourceHandle();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleMapRequest(final MapRequestEvent event) {
		if ((getPlatformRenderArea() == null)
				&& (event.getEventSource() instanceof PlatformRenderArea)) {
			setPlatformRenderArea((PlatformRenderArea) event.getEventSource());
		}
		setVisibility(true);
		requestVisibilityChange();
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleUnmapNotify(final UnmappedNotifyEvent event) {
		setVisibility(false);
		this.doUpdateVisibility(false);
	}

	/**
	 * 
	 * @param event
	 * 
	 */
	protected void handleConfigureRequest(final ConfigureRequestEvent event) {
		if (event.configureX()) {
			setRelativeX(event.configureValueX());
		}
		if (event.configureY()) {
			setRelativeY(event.configureValueY());
		}
		if (event.configureWidth()) {
			setWidth(event.configureValueWidth());
		}
		if (event.configureHeight()) {
			setHeight(event.configureValueHeight());
		}
		requestMoveResize();
	}

	@Override
	public RenderAreaGeoExecutor getGeoExecutor() {
		return this.renderAreaGeoExecutor;
	}

	@Override
	protected void setPlatformRenderArea(
			final PlatformRenderArea platformRenderArea) {
		super.setPlatformRenderArea(platformRenderArea);

		syncGeoToPlatformRenderAreaGeo();

		platformRenderArea.propagateEvent(
				EventPropagator.NOTIFY_CHANGED_WINDOW_PROPERTY,
				EventPropagator.NOTIFY_CHANGED_WINDOW_GEOMETRY,
				EventPropagator.NOTIFY_MOUSE_ENTER,
				EventPropagator.NOTIFY_MOUSE_LEAVE,
				EventPropagator.NOTIFY_CHANGED_WINDOW_FOCUS);
	}

	@Override
	public void giveInputFocus() {
		getPlatformRenderArea().setInputFocus();

	}
}
