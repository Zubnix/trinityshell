package org.hyperdrive.input.impl;

import org.hyperdrive.input.api.ManagedKeyboard;
import org.hyperdrive.input.api.ManagedMouse;

import com.google.inject.AbstractModule;

public class InputModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ManagedMouse.class).to(BaseManagedMouse.class);
		bind(ManagedKeyboard.class).to(BaseManagedKeyboard.class);

	}

}
