package org.trinity.display.x11.core.impl;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.display.api.DisplayRenderAreaFactory;
import org.trinity.foundation.display.api.DisplayResourceHandle;
import org.trinity.foundation.display.api.DisplayResourceHandleFactory;

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
				.implement(DisplayRenderArea.class, XWindow.class)
				.build(DisplayRenderAreaFactory.class));
		install(new FactoryModuleBuilder().implement(	DisplayResourceHandle.class,
														XResourceHandle.class)
				.build(DisplayResourceHandleFactory.class));
	}
}