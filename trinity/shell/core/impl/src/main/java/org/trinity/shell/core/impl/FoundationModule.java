package org.trinity.shell.core.impl;

import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.api.RenderAreaFactory;
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
		// end foundation factory bindings
	}
}
