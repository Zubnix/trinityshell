/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.event.DestroyNotify;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
@NotThreadSafe
public class XWindowCache {

	private class DestroyListener {
		private final XWindow window;

		public DestroyListener(final XWindow window) {
			this.window = window;
		}

		@Subscribe
		public void destroyed(final DestroyNotify destroyNotify) {
			XWindowCache.this.xWindows.invalidate(this.window.getDisplaySurfaceHandle().getNativeHandle());
			this.window.unregister(this);
		}
	}

	private final Cache<Integer, XWindow> xWindows = CacheBuilder.newBuilder().concurrencyLevel(1).build();
	public final Map<Integer, XWindow> windows = new HashMap<Integer, XWindow>();

	private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	XWindowCache(final DisplaySurfaceFactory displaySurfaceFactory) {
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	public XWindow getWindow(final int windowId) {

		final Integer windowID = Integer.valueOf(windowId);
		final DisplaySurfaceHandle resourceHandle = new XWindowHandle(windowID);
		XWindow window = null;
		try {
			window = this.xWindows.get(	Integer.valueOf(windowId),
										new Callable<XWindow>() {
											@Override
											public XWindow call() {
												final XWindow xWindow = (XWindow) XWindowCache.this.displaySurfaceFactory.createDisplaySurface(resourceHandle);
												xWindow.register(new DestroyListener(xWindow));
												return xWindow;
											}
										});
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
		return window;
	}

	public boolean isPresent(final int windowId) {
		return this.xWindows.getIfPresent(Integer.valueOf(windowId)) != null;
	}
}
