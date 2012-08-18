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

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.display.api.event.GeometryRequestEvent;
import org.trinity.foundation.display.api.event.HideNotifyEvent;
import org.trinity.foundation.display.api.event.ShowRequestEvent;
import org.trinity.foundation.shared.geometry.api.Rectangle;
import org.trinity.shell.core.api.AbstractShellRenderArea;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.geo.api.ShellGeoExecutor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>ShellClient</code> wraps a {@link DisplayRenderArea} that was created
 * by an independent program. As such the visual content can not be directly
 * manipulated. A <code>ShellClient</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellClient</code> provides
 * functionality to manage and manipulate the geometry and visibility of the
 * <code>PlatformRenderArea</code> it wraps.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ShellClient extends AbstractShellRenderArea {

	private final ShellGeoExecutor renderAreaGeoExecutor;

	/**
	 * Create a new <code>ShellClient</code> from a foreign
	 * <code>PlatformRenderArea</code>.
	 * 
	 * @param managedDisplay
	 *            The {@link ShellDisplay} where this <code>ShellClient</code>
	 *            lives on.
	 * @param clientWindow
	 *            The foreign {@link DisplayRenderArea}.
	 */
	@Inject
	ShellClient(final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
				final EventBus eventBus,
				@Named("shellRootRenderArea") final ShellRenderArea root,
				@Named("ShellRenderAreaGeoExecutor") final ShellGeoExecutor shellGeoExecutor,
				@Assisted final DisplayRenderArea platformRenderArea) {
		super(eventBus, shellDisplayEventDispatcher);
		this.renderAreaGeoExecutor = shellGeoExecutor;
		setPlatformRenderArea(platformRenderArea);
		setParent(root);
		doReparent(false);
	}

	@Subscribe
	public void handleMapRequest(final ShowRequestEvent event) {
		if ((getDisplayRenderArea() == null)
				&& (event.getEventSource() instanceof DisplayRenderArea)) {
			setPlatformRenderArea((DisplayRenderArea) event.getEventSource());
		}
		requestShow();
	}

	@Subscribe
	public void handleUnmapNotify(final HideNotifyEvent event) {
		doHide(false);
	}

	@Subscribe
	public void handleConfigureRequest(final GeometryRequestEvent event) {
		final Rectangle rectangle = event.getGeometry();
		if (event.configureX()) {
			setX(rectangle.getX());
		}
		if (event.configureY()) {
			setY(rectangle.getY());
		}
		if (event.configureWidth()) {
			setWidth(rectangle.getWidth());
		}
		if (event.configureHeight()) {
			setHeight(rectangle.getHeight());
		}
		requestMoveResize();
	}

	@Override
	public ShellGeoExecutor getGeoExecutor() {
		return this.renderAreaGeoExecutor;
	}

	@Override
	protected void setPlatformRenderArea(final DisplayRenderArea platformRenderArea) {
		super.setPlatformRenderArea(platformRenderArea);

		syncGeoToDisplayRenderArea();

		// platformRenderArea
		// .selectEvent( DisplayEventSelector.NOTIFY_CHANGED_WINDOW_PROPERTY,
		// DisplayEventSelector.NOTIFY_CHANGED_WINDOW_GEOMETRY,
		// DisplayEventSelector.NOTIFY_MOUSE_ENTER,
		// DisplayEventSelector.NOTIFY_MOUSE_LEAVE,
		// DisplayEventSelector.NOTIFY_CHANGED_WINDOW_FOCUS);
	}
}
