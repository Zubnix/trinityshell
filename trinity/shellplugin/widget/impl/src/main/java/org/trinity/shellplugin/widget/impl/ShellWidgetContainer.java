package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.node.ShellNodeExecutor;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(value = @Named("ShellWidgetContainer"))
public class ShellWidgetContainer extends BaseShellWidget {

	@Inject
	ShellWidgetContainer(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
							@Named("ShellWidgetContainerView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
	}
}