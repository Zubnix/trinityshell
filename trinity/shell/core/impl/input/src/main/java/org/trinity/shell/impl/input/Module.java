package org.trinity.shell.impl.input;

import org.trinity.shell.api.input.ShellKeysBinding;
import org.trinity.shell.api.input.KeysBindingFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(	ShellKeysBinding.class,
														ShellKeysBindingImpl.class).build(KeysBindingFactory.class));
	}
}