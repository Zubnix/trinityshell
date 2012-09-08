package org.trinity.shellplugin.widget.api;

import org.trinity.shellplugin.widget.api.mvvm.ViewSignal;
import org.trinity.shellplugin.widget.api.mvvm.UpdateVisualHandler;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(ViewSignal.class),
						new UpdateVisualHandler());
	}
}