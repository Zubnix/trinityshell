package org.trinity.shell.core.impl;

import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.core.api.RenderAreaFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class CoreShellModuleImpl extends AbstractModule {
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	RenderArea.class,
														ClientWindow.class)
				.build(RenderAreaFactory.class));
	}
}
