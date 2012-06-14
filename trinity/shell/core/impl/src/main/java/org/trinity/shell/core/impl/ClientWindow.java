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
package org.trinity.shell.core.impl;

import org.trinity.core.event.api.EventHandler;
import org.trinity.foundation.display.api.DisplayEventSelector;
import org.trinity.foundation.display.api.PlatformRenderArea;
import org.trinity.foundation.display.api.event.ConfigureRequestEvent;
import org.trinity.foundation.display.api.event.DisplayEventType;
import org.trinity.foundation.display.api.event.MapRequestEvent;
import org.trinity.foundation.display.api.event.UnmappedNotifyEvent;
import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.widget.api.Root;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

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
final class ClientWindow extends AbstractRenderArea {

	private final GeoExecutor renderAreaGeoExecutor;

	/**
	 * Create a new <code>ClientWindow</code> from a foreign
	 * <code>PlatformRenderArea</code>.
	 * 
	 * @param managedDisplay
	 *            The {@link ManagedDisplay} where this
	 *            <code>ClientWindow</code> lives on.
	 * @param clientWindow
	 *            The foreign {@link PlatformRenderArea}.
	 */
	@Inject
	protected ClientWindow(	final ManagedDisplay managedDisplay,
							final Root root,
							@Assisted final PlatformRenderArea platformRenderArea,
							@Named("RenderArea") final GeoExecutor geoExecutor) {
		setManagedDisplay(managedDisplay);
		setPlatformRenderArea(platformRenderArea);

		this.renderAreaGeoExecutor = geoExecutor;
		setParent(root);
		doUpdateParentValue(false);
		initEventHandlers();
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
		}, DisplayEventType.MAP_REQUEST);
		this.addEventHandler(new EventHandler<ConfigureRequestEvent>() {
			@Override
			public void handleEvent(final ConfigureRequestEvent event) {
				ClientWindow.this.handleConfigureRequest(event);

			}
		}, DisplayEventType.CONFIGURE_REQUEST);

		this.addEventHandler(new EventHandler<UnmappedNotifyEvent>() {
			@Override
			public void handleEvent(final UnmappedNotifyEvent event) {
				ClientWindow.this.handleUnmapNotify(event);
			}
		}, DisplayEventType.UNMAP_NOTIFY);
	}

	/**
	 * @param event
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
	 * @param event
	 */
	protected void handleUnmapNotify(final UnmappedNotifyEvent event) {
		setVisibility(false);
		this.doUpdateVisibility(false);
	}

	/**
	 * @param event
	 */
	protected void handleConfigureRequest(final ConfigureRequestEvent event) {
		if (event.isXSet()) {
			setX(event.getX());
		}
		if (event.isYSet()) {
			setY(event.getY());
		}
		if (event.isWidthSet()) {
			setWidth(event.getWidth());
		}
		if (event.isHeightSet()) {
			setHeight(event.getHeight());
		}
		requestMoveResize();
	}

	@Override
	public GeoExecutor getGeoExecutor() {
		return this.renderAreaGeoExecutor;
	}

	@Override
	protected void setPlatformRenderArea(final PlatformRenderArea platformRenderArea) {
		super.setPlatformRenderArea(platformRenderArea);

		syncGeoToPlatformRenderAreaGeo();

		platformRenderArea
				.propagateEvent(DisplayEventSelector.NOTIFY_CHANGED_WINDOW_PROPERTY,
								DisplayEventSelector.NOTIFY_CHANGED_WINDOW_GEOMETRY,
								DisplayEventSelector.NOTIFY_MOUSE_ENTER,
								DisplayEventSelector.NOTIFY_MOUSE_LEAVE,
								DisplayEventSelector.NOTIFY_CHANGED_WINDOW_FOCUS);
	}
}
