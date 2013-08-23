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
import javax.inject.Provider;

import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.DisplaySurfacePreparation;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
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
	private final ListeningExecutorService displayExecutor;
	private final Provider<DisplaySurfacePreparation> displaySurfacePreparationProvider;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	XWindowPoolImpl(@DisplayExecutor final ListeningExecutorService displayExecutor,
					final Provider<DisplaySurfacePreparation> displaySurfacePreparationProvider,
					final DisplaySurfaceFactory displaySurfaceFactory) {
		this.displayExecutor = displayExecutor;
		this.displaySurfacePreparationProvider = displaySurfacePreparationProvider;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public DisplaySurface getDisplaySurface(final Object nativeHandle) {

		final DisplaySurfaceHandle resourceHandle = new XWindowHandle(nativeHandle);
		XWindow window = null;
		try {
			window = this.xWindows.get(	nativeHandle,
										new Callable<XWindow>() {
											@Override
											public XWindow call() {
												LOG.debug(	"Xwindow={} added to cache.",
															nativeHandle);

												final XWindow xWindow = (XWindow) XWindowPoolImpl.this.displaySurfaceFactory
														.createDisplaySurface(resourceHandle);
												xWindow.register(new DestroyListener(xWindow));
												return xWindow;
											}
										});
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
		return window;
	}

	public boolean isPresent(final Object nativeHandle) {
		return this.xWindows.getIfPresent(nativeHandle) != null;
	}

	@Override
	public ListenableFuture<DisplaySurfacePreparation> prepareDisplaySurface() {

		return displayExecutor.submit(new Callable<DisplaySurfacePreparation>() {
			@Override
			public DisplaySurfacePreparation call() throws Exception {
				return XWindowPoolImpl.this.displaySurfacePreparationProvider.get();
			}
		});
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
