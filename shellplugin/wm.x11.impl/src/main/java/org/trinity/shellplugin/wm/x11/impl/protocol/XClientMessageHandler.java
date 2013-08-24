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

package org.trinity.shellplugin.wm.x11.impl.protocol;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.api.XEventHandler;

import com.google.common.base.Optional;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
public class XClientMessageHandler implements XEventHandler {

	private static final Integer EVENT_CODE = XCB_CLIENT_MESSAGE;
	private final DisplaySurfacePool displaySurfacePool;

	@Inject
	XClientMessageHandler(final DisplaySurfacePool displaySurfacePool) {
		this.displaySurfacePool = displaySurfacePool;
	}

	@Override
	public Optional<DisplayEvent> handle(@Nonnull final xcb_generic_event_t event) {
		final xcb_client_message_event_t client_message_event_t = new xcb_client_message_event_t(	xcb_generic_event_t.getCPtr(event),
																									false);
		displaySurfacePool.getDisplaySurface(client_message_event_t.getWindow()).post(client_message_event_t);
		// no conversion possible
		return Optional.absent();
	}

	@Override
	public Optional<AsyncListenable> getTarget(@Nonnull final xcb_generic_event_t event) {
		// no conversion so no target needed
		return Optional.absent();
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}
