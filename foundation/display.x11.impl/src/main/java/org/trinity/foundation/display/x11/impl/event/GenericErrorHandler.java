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

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Bind(multiple = true)
@Singleton
@Immutable
public class GenericErrorHandler implements XEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GenericErrorHandler.class);

    private final Integer eventCode = 0;
    private final EventBus xEventBus;

    @Inject
    GenericErrorHandler(@XEventBus final EventBus xEventBus) {
		this.xEventBus = xEventBus;
	}

	@Override
    public Optional<DisplayEvent> handle(@Nonnull final xcb_generic_event_t event_t) {
        final xcb_generic_error_t request_error = new xcb_generic_error_t(	xcb_generic_event_t.getCPtr(event_t),
																			false);
		this.xEventBus.post(request_error);

        LOG.error(XcbErrorUtil.toString(request_error));
        return Optional.absent();
    }

    @Override
    public Optional<Listenable> getTarget(@Nonnull final xcb_generic_event_t event_t) {
        // TODO return display server?
		return Optional.absent();
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}

}
