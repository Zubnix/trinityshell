/*
* Trinity Window Manager and DesktopImpl Shell Copyright (C) 2012 Erik De
* Rijcke This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by the Free
* Software Foundation, either version 3 of the License, or (at your option) any
* later version. This program is distributed in the hope that it will be
* useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
* Public License for more details. You should have received a copy of the GNU
* General Public License along with this program. If not, see
* <http://www.gnu.org/licenses/>.
*/
package org.trinity.bootstrap;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.jogamp.newt.opengl.GLWindow;
import dagger.ObjectGraph;

import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.output.CompositorFactory;
import org.trinity.wayland.output.ShmRenderer;
import org.trinity.wayland.input.newt.GLWindowSeat;
import org.trinity.wayland.input.newt.GLWindowSeatFactory;
import org.trinity.platform.newt.GLWindowFactory;
import org.trinity.wayland.output.ShmRendererFactory;
import org.trinity.wayland.protocol.WlCompositor;
import org.trinity.wayland.protocol.WlCompositorFactory;
import org.trinity.wayland.output.gl.GLRenderEngine;
import org.trinity.wayland.output.gl.GLRenderEngineFactory;
import xcb4j.LibXcbLoader;

import javax.inject.Inject;
import java.util.Set;

public class EntryPoint {

    private final ServiceManager serviceManager;
    private final GLWindowFactory glWindowFactory;
    private final GLRenderEngineFactory glRenderEngineFactory;
    private final ShmRendererFactory wlShmRendererFactory;
    private final CompositorFactory wlShellCompositorFactory;
    private final WlCompositorFactory wlCompositorFactory;
    private final GLWindowSeatFactory glWindowSeatFactory;

    @Inject
    EntryPoint( final GLWindowFactory glWindowFactory,
                final GLRenderEngineFactory glRenderEngineFactory,
                final ShmRendererFactory wlShmRendererFactory,
                final CompositorFactory wlShellCompositorFactory,
                final WlCompositorFactory wlCompositorFactory,
                final GLWindowSeatFactory glWindowSeatFactory,
                final Set<Service> services) {
        this.glWindowFactory = glWindowFactory;
        this.glRenderEngineFactory = glRenderEngineFactory;
        this.wlShmRendererFactory = wlShmRendererFactory;
        this.wlShellCompositorFactory = wlShellCompositorFactory;
        this.wlCompositorFactory = wlCompositorFactory;
        this.glWindowSeatFactory = glWindowSeatFactory;

        //group services that will drive compositor
        this.serviceManager = new ServiceManager(services);
    }

    private void enter() {
        //create an output
        //create an X opengl enabled window
        final GLWindow glWindow = glWindowFactory.create();

        //setup our render engine
        //create an opengl renderengine that uses shm buffers and outputs to an X opengl window
        final GLRenderEngine glRenderEngine = glRenderEngineFactory.create(glWindow);
        //create an shm renderer that passes on shm buffers to it's render implementation
        final ShmRenderer shmRenderer = wlShmRendererFactory.create(glRenderEngine);

        //setup compositing
        //create a compositor with shell and scene logic
        final Compositor compositor = wlShellCompositorFactory.create(shmRenderer);
        //create a wayland compositor that delegates it's requests to a shell implementation.
        final WlCompositor wlCompositor = wlCompositorFactory.create(compositor);

        //setup seat
        //create a seat that listens for input on the X opengl window and passes it on to a wayland seat.
        final GLWindowSeat glWindowSeat = glWindowSeatFactory.create(glWindow,
                                                                     compositor);

        //start all services
        this.serviceManager.startAsync();
    }

    public static void main(final String[] args) {
        LibXcbLoader.load();

		final TrinityShellModule trinityShellModule = new TrinityShellModule();
		final ObjectGraph objectGraph = ObjectGraph.create(trinityShellModule);
        trinityShellModule.setObjectGraph(objectGraph);

		objectGraph.get(EntryPoint.class).enter();
    }
}