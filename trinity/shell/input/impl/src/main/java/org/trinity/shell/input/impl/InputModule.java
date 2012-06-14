package org.trinity.shell.input.impl;

import org.trinity.shell.input.api.KeyBinding;
import org.trinity.shell.input.api.KeyBindingFactory;
import org.trinity.shell.input.api.KeyInputStringBuilder;
import org.trinity.shell.input.api.ManagedKeyboard;
import org.trinity.shell.input.api.ManagedMouse;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class InputModule extends AbstractModule {

	@Override
	protected void configure() {
		// start input bindings
		bind(ManagedMouse.class).to(ManagedMouseImpl.class);
		bind(ManagedKeyboard.class).to(ManagedKeyboardImpl.class);
		bind(KeyInputStringBuilder.class).to(KeyInputStringBuilderImpl.class);
		// end input bindings

		// start input factory bindings
		install(new FactoryModuleBuilder().implement(	KeyBinding.class,
														KeyBindingImpl.class)
				.build(KeyBindingFactory.class));
		// end input factory bindings
	}
}