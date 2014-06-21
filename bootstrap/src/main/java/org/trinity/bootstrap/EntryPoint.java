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
import dagger.ObjectGraph;
import pixman4j.LibPixmanLoader;
import xcb4j.LibXcbLoader;

import javax.inject.Inject;
import java.util.Set;

public class EntryPoint {

    private final ServiceManager serviceManager;

    @Inject
    EntryPoint(final Set<Service> services) { this.serviceManager = new ServiceManager(services); }

    private void enter() { this.serviceManager.startAsync(); }

    public static void main(final String[] args) {
        LibXcbLoader.load();
        LibPixmanLoader.load();

		final TrinityShellModule trinityShellModule = new TrinityShellModule();
		final ObjectGraph objectGraph = ObjectGraph.create(trinityShellModule);

        trinityShellModule.setObjectGraph(objectGraph);
        objectGraph.injectStatics();

		objectGraph.get(EntryPoint.class).enter();
    }
}