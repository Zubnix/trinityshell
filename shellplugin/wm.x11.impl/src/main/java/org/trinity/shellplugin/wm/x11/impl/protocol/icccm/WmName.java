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

package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import com.google.common.base.Optional;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name_reply;

@Singleton
@ThreadSafe
public class WmName extends AbstractCachedProtocol<xcb_icccm_get_text_property_reply_t> {

	private static final Logger LOG = LoggerFactory.getLogger(WmName.class);
	private final XEventChannel xEventChannel;

	@Inject
	WmName(final XEventChannel xEventChannel,
		   final XAtomCache xAtomCache
		  ) {
		super(
				xAtomCache,
				"WM_NAME");

		this.xEventChannel = xEventChannel;
	}

	@Override
	protected Optional<xcb_icccm_get_text_property_reply_t> queryProtocol(final DisplaySurface xWindow) {
		final int window = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_property_cookie = xcb_icccm_get_wm_name(this.xEventChannel
																							.getConnectionReference(),
																					window);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_icccm_get_text_property_reply_t prop = new xcb_icccm_get_text_property_reply_t();

		final short stat = xcb_icccm_get_wm_name_reply(WmName.this.xEventChannel.getConnectionReference(),
													   get_property_cookie,
													   prop,
													   e);
		if(stat == 0) {
			LOG.error("Error retrieving wm_name reply from client={}",
					  window);
			return Optional.absent();
		}

		return Optional.of(prop);
	}
}
