package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.DisplaySurfaceHandleFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("xEventBus"))
				.asEagerSingleton();
		bind(EventBus.class).annotatedWith(Names.named("displayEventBus"))
				.asEagerSingleton();

		install(new FactoryModuleBuilder()
				.implement(DisplaySurface.class, XWindow.class)
				.build(DisplaySurfaceFactory.class));
		install(new FactoryModuleBuilder().implement(	DisplaySurfaceHandle.class,
														XWindowHandle.class)
				.build(DisplaySurfaceHandleFactory.class));
	}
}