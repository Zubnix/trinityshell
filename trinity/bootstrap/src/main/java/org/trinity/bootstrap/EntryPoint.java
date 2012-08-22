package org.trinity.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.devsurf.injection.guice.DynamicModule;
import de.devsurf.injection.guice.scanner.PackageFilter;
import de.devsurf.injection.guice.scanner.StartupModule;
import de.devsurf.injection.guice.scanner.asm.ASMClasspathScanner;

public class EntryPoint {

	public static void main(final String[] args) {

		new EntryPoint(args);
	}

	public EntryPoint(final String[] args) {

		// TODO command line args parsing

		final Injector injector = Guice.createInjector(StartupModule
				.create(ASMClasspathScanner.class,
						PackageFilter.create("org.trinity")));
		final DynamicModule dynamicModule = injector
				.getInstance(DynamicModule.class);
		final Injector annotationBindingsInjector = injector
				.createChildInjector(dynamicModule);

		final ShellPluginsRunner shellPluginsRunner = annotationBindingsInjector
				.getInstance(ShellPluginsRunner.class);
		shellPluginsRunner.startAllShellPlugins();
	}
}
