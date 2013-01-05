package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		final PropertyChangedSignalDispatcher viewSignalDispatcher = new PropertyChangedSignalDispatcher();
		requestInjection(viewSignalDispatcher);
		bindInterceptor(Matchers.any(),
						Matchers.annotatedWith(PropertyChanged.class),
						viewSignalDispatcher);
	}
}