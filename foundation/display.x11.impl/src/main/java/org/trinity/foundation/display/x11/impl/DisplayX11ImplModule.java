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

import dagger.Module;
import dagger.Provides;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurfaceFactory;
import org.trinity.foundation.api.display.DisplaySurfacePool;
import org.trinity.foundation.display.x11.api.XEventChannel;

import javax.inject.Singleton;

@Module(
        injects = {
                //display api
                DisplaySurfaceFactory.class,
                DisplaySurfacePool.class,
                Display.class,

                //x11 display api
                XEventChannel.class,
        },
        complete = true,
        library = true
)
public class DisplayX11ImplModule {

    @Provides
    @Singleton
    DisplaySurfacePool provideDisplaySurfacePool(final DisplaySurfacePoolImpl xWindowPool) {
        return xWindowPool;
    }

    @Provides
    @Singleton
    Display provideDisplay(final XDisplayImpl xDisplay) {
        return xDisplay;
    }

    @Provides
    @Singleton
    DisplaySurfaceFactory provideDisplaySurfaceFactory(final XDisplaySurfaceFactory xDisplaySurfaceFactory) {
        return xDisplaySurfaceFactory;
    }

    @Provides
    @Singleton
	XEventChannel provideXConnection(final XEventChannelImpl xEventChannel) {
        return xEventChannel;
    }
}
