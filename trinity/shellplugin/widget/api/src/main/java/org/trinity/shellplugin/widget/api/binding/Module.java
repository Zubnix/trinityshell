package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public final class Module extends AbstractModule {

	@Override
	protected void configure() {
		final ViewAttributeSignalDispatcher viewSignalDispatcher = new ViewAttributeSignalDispatcher();
		requestInjection(viewSignalDispatcher);
		bindInterceptor(Matchers.subclassesOf(PaintableSurfaceNode.class),
						Matchers.annotatedWith(ViewAttributeChanged.class),
						viewSignalDispatcher);
	}
}