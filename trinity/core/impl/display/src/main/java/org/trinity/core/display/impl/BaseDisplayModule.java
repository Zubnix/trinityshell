package org.trinity.core.display.impl;

import org.trinity.core.display.api.Display;
import org.trinity.core.display.api.PropertyInstance;

import com.google.inject.AbstractModule;

public class BaseDisplayModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Display.class).to(BaseDisplay.class);
		bind(PropertyInstance.class).to(BasePropertyInstance.class);
		bind(BasePropertyInstance.class).to(PropertyInstanceAtoms.class);
		bind(BasePropertyInstance.class).to(PropertyInstanceNumber.class);
		bind(BasePropertyInstance.class).to(PropertyInstanceNumber.class);
		bind(BasePropertyInstance.class).to(
				PropertyInstancePlatformRenderArea.class);
		bind(BasePropertyInstance.class).to(
				PropertyInstancePlatformRenderAreas.class);
		bind(BasePropertyInstance.class).to(PropertyInstanceText.class);
		bind(BasePropertyInstance.class).to(PropertyInstanceTexts.class);

	}

}
