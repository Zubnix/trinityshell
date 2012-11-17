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
package org.trinity.bootstrap;

import org.trinity.shell.api.plugin.ShellPluginsRunner;

import xcb4j.LibXcbLoader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import de.devsurf.injection.guice.scanner.PackageFilter;
import de.devsurf.injection.guice.scanner.StartupModule;
import de.devsurf.injection.guice.scanner.asm.ASMClasspathScanner;

public class EntryPoint {

	public static void main(final String[] args) {

		LibXcbLoader.load();

		final Injector injector = Guice.createInjector(	Stage.PRODUCTION,
														StartupModule.create(	ASMClasspathScanner.class,
																				PackageFilter.create("org.trinity")));

		final ShellPluginsRunner shellPluginsRunner = injector.getInstance(ShellPluginsRunner.class);
		shellPluginsRunner.startAll();

	}
}