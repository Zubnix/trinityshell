package org.trinity.shell.input.impl;

import org.trinity.shell.input.api.KeysBinding;
import org.trinity.shell.input.api.KeysBindingFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	KeysBinding.class,
														KeysBindingImpl.class)
				.build(KeysBindingFactory.class));
	}
}