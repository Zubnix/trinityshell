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

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_hints;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_hints_reply;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_wm_hints_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

@Singleton
@Bind(to = @To(IMPLEMENTATION))
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public class WmHints extends AbstractCachedProtocol<xcb_icccm_wm_hints_t> {

	private static final Logger LOG = LoggerFactory.getLogger(WmHints.class);
	private final XConnection xConnection;
	private final ListeningExecutorService displayExecutor;

	@Inject
	WmHints(@DisplayExecutor final ListeningExecutorService displayExecutor,
			final XConnection xConnection,
			final XAtomCache xAtomCache) {
		super(	displayExecutor,
				xAtomCache,
				"WM_HINTS");
		this.displayExecutor = displayExecutor;
		this.xConnection = xConnection;
	}

	@Override
	protected ListenableFuture<Optional<xcb_icccm_wm_hints_t>> queryProtocol(final DisplaySurface xWindow) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_wm_hints_cookie = xcb_icccm_get_wm_hints(	this.xConnection
																								.getConnectionReference()
																								.get(),
																						winId.intValue());

		return this.displayExecutor.submit(new Callable<Optional<xcb_icccm_wm_hints_t>>() {
			@Override
			public Optional<xcb_icccm_wm_hints_t> call() throws Exception {
				final xcb_icccm_wm_hints_t hints = new xcb_icccm_wm_hints_t();
				final xcb_generic_error_t e = new xcb_generic_error_t();

				final short stat = xcb_icccm_get_wm_hints_reply(WmHints.this.xConnection.getConnectionReference().get(),
																get_wm_hints_cookie,
																hints,
																e);
				if (xcb_generic_error_t.getCPtr(e) != 0) {
					final String errorString = XcbErrorUtil.toString(e);
					LOG.error(errorString);
					return Optional.absent();
				}

				if (stat == 0) {
					LOG.error(	"Failed to read wm_hints reply from client={}",
								winId);
					return Optional.absent();
				}

				return Optional.of(hints);
			}
		});
	}
}
