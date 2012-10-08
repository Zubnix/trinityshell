package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.ShellWidgetView;
import org.trinity.shellplugin.widget.api.binding.ShellWidgetStyled;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(value = @Named("ShellWidgetContainer"), to = @To(Type.SUPER))
public class ShellWidgetContainer extends ShellWidgetStyled {

	@Inject
	ShellWidgetContainer(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							@Named("ShellWidgetContainerView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
	}
}
