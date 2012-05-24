package org.trinity.shell.foundation.impl;

import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.foundation.api.PropertyManipulator;
import org.trinity.shell.foundation.api.PropertyManipulatorFactory;
import org.trinity.shell.foundation.api.RenderArea;
import org.trinity.shell.foundation.api.RenderAreaFactory;
import org.trinity.shell.geo.api.GeoExecutor;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class FoundationModule extends AbstractModule {
	@Override
	protected void configure() {
		// start foundation bindings
		bind(ManagedDisplay.class).to(ManagedDisplayImpl.class);
		bind(GeoExecutor.class).annotatedWith(Names.named("RenderArea"))
				.to(RenderAreaGeoExecutorImpl.class);
		// end foundation bindings

		// start foundation factory bindings
		install(new FactoryModuleBuilder().implement(	RenderArea.class,
														ClientWindow.class)
				.build(RenderAreaFactory.class));
		install(new FactoryModuleBuilder()
				.implement(	PropertyManipulator.class,
							PropertyManipulatorImpl.class)
				.build(PropertyManipulatorFactory.class));
		// end foundation factory bindings
	}
}
