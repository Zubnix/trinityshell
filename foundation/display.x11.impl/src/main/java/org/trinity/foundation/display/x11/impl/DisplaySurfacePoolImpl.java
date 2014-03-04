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

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.display.x11.api.XEventChannel;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@ThreadSafe
public class DisplaySurfacePoolImpl implements DisplaySurfacePool {

	private static final Logger LOG = LoggerFactory.getLogger(DisplaySurfacePoolImpl.class);

	private final Map<DisplaySurfaceHandle, DisplaySurface> displaySurfaces = new HashMap<>();

	private final XEventChannel  xEventChannel;
	private final XWindowFactory xWindowFactory;

	@Inject
	DisplaySurfacePoolImpl(final XEventChannel xEventChannel,
						   final XWindowFactory xWindowFactory) {
		this.xEventChannel = xEventChannel;
		this.xWindowFactory = xWindowFactory;
	}

	@Override
	public DisplaySurface get(final DisplaySurfaceHandle displaySurfaceHandle) {

		DisplaySurface window = this.displaySurfaces.get(displaySurfaceHandle);
		if(window == null) {
			window = registerNewDisplaySurface(displaySurfaceHandle);
		}
		return window;
	}

	private DisplaySurface registerNewDisplaySurface(final DisplaySurfaceHandle displaySurfaceHandle) {
		LOG.debug("Xwindow={} added to cache.",
				  displaySurfaceHandle);

		final DisplaySurface window = this.xWindowFactory.create(displaySurfaceHandle);
		window.register(new DestroyListener(window));
		this.displaySurfaces.put(displaySurfaceHandle,
								 window);
		return window;
	}

	private void unregisterDisplaySurface(final DisplaySurface displaySurface) {
		this.displaySurfaces.remove(displaySurface.getDisplaySurfaceHandle().getNativeHandle().hashCode());
		displaySurface.unregister(this);
	}

	@Override
	public Boolean isPresent(final DisplaySurfaceHandle displaySurfaceHandle) {
		return this.displaySurfaces.containsKey(displaySurfaceHandle.getNativeHandle().hashCode());
	}

	private class DestroyListener {
		private final DisplaySurface window;

		public DestroyListener(final DisplaySurface displaySurface) {
			this.window = displaySurface;
		}

		@Subscribe
		public void destroyed(final DestroyNotify destroyNotify) {
				unregisterDisplaySurface(this.window);
		}
	}
}
