/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.display.x11.impl.event;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcbConstants.XCB_MAP_REQUEST;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY;

import java.nio.ByteBuffer;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.impl.XWindowPoolImpl;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@Immutable
public class MapRequestHandler implements XEventHandler {

	private static final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
			| XCB_EVENT_MASK_STRUCTURE_NOTIFY;
	private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
			.putInt(CLIENT_EVENT_MASK);
	private static final Logger LOG = LoggerFactory.getLogger(MapRequestHandler.class);
	private static final Integer EVENT_CODE = XCB_MAP_REQUEST;
	private final EventBus xEventBus;
	private final XConnection xConnection;
	private final XWindowPoolImpl xWindowCache;
	private final Display display;

	@Inject
	MapRequestHandler(	@XEventBus final EventBus xEventBus,
						final XConnection xConnection,
						final XWindowPoolImpl xWindowCache,
						final Display display) {
		this.xEventBus = xEventBus;
		this.xConnection = xConnection;
		this.xWindowCache = xWindowCache;
		this.display = display;
	}

	@Override
	public Optional<ShowRequest> handle(final xcb_generic_event_t event) {

		final xcb_map_request_event_t map_request_event = cast(event);

		LOG.debug(	"Received X event={}",
					map_request_event.getClass().getSimpleName());

		this.xEventBus.post(map_request_event);

		return Optional.of(new ShowRequest());
	}

	private xcb_map_request_event_t cast(final xcb_generic_event_t event) {
		return new xcb_map_request_event_t(	xcb_generic_event_t.getCPtr(event),
											false);
	}

	@Override
	public Optional<DisplaySurface> getTarget(final xcb_generic_event_t event_t) {
		final xcb_map_request_event_t map_request_event_t = cast(event_t);
		final int windowId = map_request_event_t.getWindow();
		final XWindowHandle xWindowHandle = new XWindowHandle(windowId);
		final boolean present = this.xWindowCache.isPresent(xWindowHandle);
		final DisplaySurface displayEventTarget = this.xWindowCache.getDisplaySurface(xWindowHandle);
		if (!present) {
			configureClientEvents(displayEventTarget);
			// this is a bit of a dirty hack to work around X's model of client
			// discovery.
			final DisplaySurfaceCreationNotify displaySurfaceCreationNotify = new DisplaySurfaceCreationNotify(displayEventTarget,true);
			this.display.post(displaySurfaceCreationNotify);
		}
		return Optional.of(displayEventTarget);
	}

	private void configureClientEvents(final DisplaySurface window) {
		final int winId = (Integer) window.getDisplaySurfaceHandle().getNativeHandle();

		LOG.debug(	"[winId={}] configure client evens.",
					winId);

		xcb_change_window_attributes(	this.xConnection.getConnectionReference().get(),
										winId,
										XCB_CW_EVENT_MASK,
										CLIENT_EVENTS_CONFIG_BUFFER);
		xcb_flush(this.xConnection.getConnectionReference().get());
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}
