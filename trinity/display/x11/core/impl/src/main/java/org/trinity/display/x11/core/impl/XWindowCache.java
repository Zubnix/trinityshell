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
package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.DisplaySurfaceHandleFactory;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class XWindowCache {

	// TODO make use of guava cache?

	private final Cache<Integer, XWindow> xWindows = CacheBuilder.newBuilder().softValues().build();

	public final Map<Integer, XWindow> windows = new HashMap<Integer, XWindow>();

	private final DisplaySurfaceHandleFactory displaySurfaceHandleFactory;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	@Inject
	XWindowCache(	final DisplaySurfaceHandleFactory resourceHandleFactory,
					final DisplaySurfaceFactory displayResourceFactory) {
		this.displaySurfaceHandleFactory = resourceHandleFactory;
		this.displaySurfaceFactory = displayResourceFactory;
	}

	public XWindow getWindow(final int windowId) {

		XWindow window = null;
		final Integer windowID = Integer.valueOf(windowId);
		final DisplaySurfaceHandle resourceHandle = XWindowCache.this.displaySurfaceHandleFactory
				.createDisplaySurfaceHandle(windowID);
		try {
			window = this.xWindows.get(	Integer.valueOf(windowId),
										new Callable<XWindow>() {
											@Override
											public XWindow call() throws Exception {
												return (XWindow) XWindowCache.this.displaySurfaceFactory
														.createDisplaySurface(resourceHandle);
											}
										});
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		}
		return window;
	}
}
