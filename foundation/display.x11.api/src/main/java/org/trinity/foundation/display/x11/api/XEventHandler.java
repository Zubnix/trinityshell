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
package org.trinity.foundation.display.x11.api;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.base.Optional;

/**
 * Handles and optionally converts X events to trinity display events.
 * Implementers should simply provide the event code of the X event they're
 * interested in and use Guice's 'multiple' bind mechanism to register the event
 * handler.
 * <p/>
 * Optionally the X event can be converted to a trinitiy display event in which
 * case the corresponding target should also be provided.
 */
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public interface XEventHandler {
	/**
	 * Handle an X event and optionally convert it to the corresponding trinitiy
	 * display event.
	 *
	 * @param event The X event to handle
	 * @return an optional matching trinity display event
	 */
	Optional<? extends DisplayEvent> handle(@Nonnull xcb_generic_event_t event);

	/**
	 * The target of the returned trinity display event as returned in {@link XEventHandler#handle(xcb_generic_event_t)}.
	 * @param event
	 * @return
	 */
	Optional<? extends AsyncListenable> getTarget(@Nonnull xcb_generic_event_t event);

	/**
	 * The code of the X event to handle.
	 * @return
	 */
	Integer getEventCode();
}
