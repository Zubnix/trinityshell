package org.hyperdrive.foundation.impl;

import org.hyperdrive.foundation.api.ManagedDisplay;
import org.hyperdrive.foundation.api.PropertyManipulator;

import com.google.inject.AbstractModule;

public class FoundationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ManagedDisplay.class).to(ManagedDisplayImpl.class);
		bind(PropertyManipulator.class).to(BasePropertyManipulator.class);
	}
}
