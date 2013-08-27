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
package org.trinity.foundation.display.x11.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.*;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind
@Singleton
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public class XWindowPoolImpl implements DisplaySurfacePool {

	private static final Logger LOG = LoggerFactory.getLogger(XWindowPoolImpl.class);
	public final Map<Integer, XWindow> windows = new HashMap<Integer, XWindow>();
	private final Cache<Object, XWindow> xWindows = CacheBuilder.newBuilder().concurrencyLevel(1).build();
	private final XEventPump xEventPump;
    private final Display display;
    private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	XWindowPoolImpl(final XEventPump xEventPump,
                    final Display display,
					final DisplaySurfaceFactory displaySurfaceFactory) {
		this.xEventPump = xEventPump;
        this.display = display;
        this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public DisplaySurface getDisplaySurface(final DisplaySurfaceHandle displaySurfaceHandle) {

		XWindow window = null;
		try {
			window = this.xWindows.get(	displaySurfaceHandle,
										new Callable<XWindow>() {
											@Override
											public XWindow call() {
												LOG.debug(	"Xwindow={} added to cache.",
															displaySurfaceHandle);

												final XWindow xWindow = (XWindow) XWindowPoolImpl.this.displaySurfaceFactory
														.createDisplaySurface(displaySurfaceHandle);
												xWindow.register(new DestroyListener(xWindow));
												return xWindow;
											}
										});
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
		return window;
	}

	public boolean isPresent(final DisplaySurfaceHandle displaySurfaceHandle) {
		return this.xWindows.getIfPresent(displaySurfaceHandle) != null;
	}

	@Override
	public DisplaySurfaceCreator getDisplaySurfaceCreator() {
		this.xEventPump.stop();

		return new DisplaySurfaceCreator() {
			@Override
			public void create(final DisplaySurfaceHandle displaySurfaceHandle) {
                final DisplaySurface displaySurface = getDisplaySurface(displaySurfaceHandle);
                display.post(new DisplaySurfaceCreationNotify(displaySurface,false));
			}

			@Override
			public void close() {
				xEventPump.start();
			}
		};
	}

	private class DestroyListener {
		private final XWindow window;

		public DestroyListener(final XWindow window) {
			this.window = window;
		}

		@Subscribe
		public void destroyed(final DestroyNotify destroyNotify) {
			final Integer windowId = (Integer) this.window.getDisplaySurfaceHandle().getNativeHandle();
			XWindowPoolImpl.this.xWindows.invalidate(windowId);
			this.window.unregister(this);

			LOG.debug(	"Xwindow={} removed from cache.",
						windowId);
		}
	}
}
