package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("xEventBus")).toInstance(new EventBus());
		bind(EventBus.class).annotatedWith(Names.named("displayEventBus")).toInstance(new EventBus());

		install(new FactoryModuleBuilder().implement(	DisplaySurface.class,
														XWindow.class).build(DisplaySurfaceFactory.class));
	}
}