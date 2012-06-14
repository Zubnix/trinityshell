package org.trinity.shell.input.impl;


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