package org.trinity.shellplugin.widget.api.mvvm;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		final ViewSignalDispatcher viewSignalDispatcher = new ViewSignalDispatcher();
		requestInjection(viewSignalDispatcher);
		bindInterceptor(Matchers.subclassesOf(PaintableSurfaceNode.class),
						Matchers.annotatedWith(ViewSignal.class),
						viewSignalDispatcher);
	}
}