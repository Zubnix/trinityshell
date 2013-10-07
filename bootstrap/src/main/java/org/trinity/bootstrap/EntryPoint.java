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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.onami.autobind.configuration.StartupModule;
import org.apache.onami.autobind.scanner.PackageFilter;
import org.apache.onami.autobind.scanner.asm.ASMClasspathScanner;
import org.trinity.shell.api.plugin.ShellPluginsRunner;
import org.trinity.shellplugin.wm.view.javafx.FXApplication;
import xcb4j.LibXcbLoader;

import java.util.List;
import java.util.Set;

import static com.google.inject.Stage.PRODUCTION;


public class EntryPoint extends FXApplication {

    public static void main(final String[] args) {
        LibXcbLoader.load();
        launch(args);
    }

    @Override
    public void init(final List<Module> modules) throws Exception {
        final StartupModule startupModule = StartupModule.create(ASMClasspathScanner.class,
                                                           PackageFilter.create("org.trinity"));
        modules.add(startupModule);
    }

    @Override
    protected Injector createInjector(final Set<Module> modules) {
        return Guice.createInjector(PRODUCTION,
                                    modules);
    }

    @Override
    public void postStart() {
        final ShellPluginsRunner instance = getInjector().getInstance(ShellPluginsRunner.class);
        instance.startAll();
    }
}