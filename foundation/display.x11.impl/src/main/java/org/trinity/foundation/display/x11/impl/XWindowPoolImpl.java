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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceCreator;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.api.display.event.DestroyNotify;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashMap;
import java.util.Map;

@Bind
@Singleton
@NotThreadSafe
public class XWindowPoolImpl implements DisplaySurfacePool {

    private static final Logger LOG = LoggerFactory.getLogger(XWindowPoolImpl.class);
    public final Map<Integer, XWindow> windows = new HashMap<>();
    private final XEventPump xEventPump;
    private final DisplaySurfaceFactory displaySurfaceFactory;

    @Inject
    XWindowPoolImpl(final XEventPump xEventPump,
                    final DisplaySurfaceFactory displaySurfaceFactory) {
        this.xEventPump = xEventPump;
        this.displaySurfaceFactory = displaySurfaceFactory;
    }

    @Override
    public DisplaySurface getDisplaySurface(final DisplaySurfaceHandle displaySurfaceHandle) {

        int windowHash = displaySurfaceHandle.getNativeHandle().hashCode();
        XWindow window = windows.get(windowHash);
        if(window == null) {
            LOG.debug("Xwindow={} added to cache.",
                      displaySurfaceHandle);

            window = (XWindow) XWindowPoolImpl.this.displaySurfaceFactory
                    .createDisplaySurface(displaySurfaceHandle);
            window.register(new DestroyListener(window));
            windows.put(windowHash,
                        window);
        }
        return window;
    }

    public boolean isPresent(final DisplaySurfaceHandle displaySurfaceHandle) {

        return this.windows.containsKey(displaySurfaceHandle.getNativeHandle().hashCode());
    }

    @Override
    public DisplaySurfaceCreator getDisplaySurfaceCreator() {
        this.xEventPump.stop();

        return new DisplaySurfaceCreator() {
            @Override
            public DisplaySurface reference(final DisplaySurfaceHandle displaySurfaceHandle) {
                return getDisplaySurface(displaySurfaceHandle);
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

            XWindowPoolImpl.this.windows.remove(this.window.getDisplaySurfaceHandle().getNativeHandle().hashCode());
            this.window.unregister(this);
        }
    }
}
