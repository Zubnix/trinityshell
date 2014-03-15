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
import org.trinity.foundation.display.x11.impl.render.simple.SimpleRenderModule;
import org.trinity.foundation.display.x11.impl.xeventhandler.XEventHandlersModule;

import javax.inject.Singleton;

@Module(
        includes = {
				//internal
				XEventHandlersModule.class,
				SimpleRenderModule.class
        },
        injects = {
                XSurfacePool.class,
                XEventChannel.class,
                XEventHandlers.class,
                XSeat.class,
                XTime.class,
                XWindowFactory.class
        },
        complete = false,
        library = true
)
public class X11DefaultModule {

	@Provides
	@Singleton
	XSeat provideXSeat(final XTime xTime,
					   final XEventChannel xEventChannel) {
		return new XSeat(xTime,
						 xEventChannel);
	}
}