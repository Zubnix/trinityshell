package org.trinity.shellplugin.widget.impl.view.qt;

import org.trinity.foundation.api.render.binding.view.View;

import com.google.inject.AbstractModule;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(View.class).toProvider(StyledViewProvider.class);
	}
}