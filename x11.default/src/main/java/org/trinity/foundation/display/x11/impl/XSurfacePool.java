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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.common.Listenable;
import org.trinity.foundation.display.x11.impl.render.XCompositorSimple;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ThreadSafe
public class XSurfacePool {

    private static final Logger LOG = LoggerFactory.getLogger(XSurfacePool.class);

    private final BiMap<Integer, Listenable> surfaces = HashBiMap.create(32);
    private final XCompositorSimple compositor;

    @Inject
    XSurfacePool(final XCompositorSimple compositor) {
        this.compositor = compositor;
    }

    public Listenable get(final Integer surfaceHandle) {
        Listenable surface = this.surfaces.get(surfaceHandle);
        if (surface == null) {
            surface = registerNewSurface(surfaceHandle);
        }
        return surface;
    }

    private Listenable registerNewSurface(final Integer surfaceHandle) {
        LOG.debug("Xwindow={} added to cache.",
                surfaceHandle);
        final Listenable window = this.compositor.createSurface(surfaceHandle);
        window.register(new DestroyListener(window));
        this.surfaces.put(surfaceHandle,
                window);
        return window;
    }

    private class DestroyListener {
        private final Listenable surface;

        public DestroyListener(final Listenable surface) {
            this.surface = surface;
        }

        @Subscribe
        public void destroyed(final xcb_destroy_notify_event_t destroyNotifyEvent) {
            XSurfacePool.this.surfaces.remove(surface);
            surface.unregister(this);
        }
    }
}