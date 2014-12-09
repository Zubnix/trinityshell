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
import org.trinity.wayland.WlShellCompositor;
import org.trinity.wayland.WlShellCompositorFactory;
import org.trinity.wayland.WlShmRenderer;
import org.trinity.wayland.WlShmRendererFactory;
import org.trinity.wayland.platform.newt.GLWindowFactory;
import org.trinity.wayland.protocol.WlCompositor;
import org.trinity.wayland.protocol.WlCompositorFactory;
import org.trinity.wayland.render.gl.GLRenderEngine;
import org.trinity.wayland.render.gl.GLRenderEngineFactory;
import xcb4j.LibXcbLoader;

import javax.inject.Inject;
import java.util.Set;

public class EntryPoint {

    private final ServiceManager serviceManager;
    private final GLWindowFactory glWindowFactory;
    private final GLRenderEngineFactory glRenderEngineFactory;
    private final WlShmRendererFactory wlShmRendererFactory;
    private final WlShellCompositorFactory wlShellCompositorFactory;
    private final WlCompositorFactory wlCompositorFactory;
    private final Set<Service> services;

    @Inject
    EntryPoint( final GLWindowFactory glWindowFactory,
                final GLRenderEngineFactory glRenderEngineFactory,
                final WlShmRendererFactory wlShmRendererFactory,
                final WlShellCompositorFactory wlShellCompositorFactory,
                final WlCompositorFactory wlCompositorFactory,
                final Set<Service> services) {
        this.glWindowFactory = glWindowFactory;
        this.glRenderEngineFactory = glRenderEngineFactory;
        this.wlShmRendererFactory = wlShmRendererFactory;
        this.wlShellCompositorFactory = wlShellCompositorFactory;
        this.wlCompositorFactory = wlCompositorFactory;
        this.services = services;

        //group services that will drive compositor
        this.serviceManager = new ServiceManager(services);
    }

    private void enter() {
        //create an output
        final GLWindow glWindow = glWindowFactory.create();

        //setup our render engine
        final GLRenderEngine glRenderEngine = glRenderEngineFactory.create(glWindow);
        final WlShmRenderer wlShmRenderer = wlShmRendererFactory.create(glRenderEngine);

        //setup compositing
        final WlShellCompositor wlShellCompositor = wlShellCompositorFactory.create(wlShmRenderer);
        final WlCompositor wlCompositor = wlCompositorFactory.create(wlShellCompositor);

        //TODO setup seat

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