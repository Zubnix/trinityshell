package org.trinity.bootstrap;

import org.trinity.shell.api.plugin.ShellPluginsRunner;

import xcbcustom.LibXcbLoader;

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