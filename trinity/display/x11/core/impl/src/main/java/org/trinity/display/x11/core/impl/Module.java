package org.trinity.display.x11.core.impl;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("xEventBus"))
				.asEagerSingleton();
		bind(EventBus.class).annotatedWith(Names.named("displayEventBus"))
				.asEagerSingleton();
	}
}