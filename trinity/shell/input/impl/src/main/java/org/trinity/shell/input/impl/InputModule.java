package org.trinity.shell.input.impl;

import org.trinity.shell.input.api.KeyBinding;
import org.trinity.shell.input.api.KeyBindingFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class InputModule extends AbstractModule {

	@Override
	protected void configure() {
		// start input factory bindings
		install(new FactoryModuleBuilder().implement(	KeyBinding.class,
														KeyBindingImpl.class)
				.build(KeyBindingFactory.class));
		// end input factory bindings
	}
}