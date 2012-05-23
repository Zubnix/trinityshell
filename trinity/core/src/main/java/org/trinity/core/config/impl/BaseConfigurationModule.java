package org.trinity.core.config.impl;

import org.trinity.core.config.api.DisplayConfiguration;

import com.google.inject.AbstractModule;

public class BaseConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DisplayConfiguration.class).to(BaseDisplayConfiguration.class);
	}

}
