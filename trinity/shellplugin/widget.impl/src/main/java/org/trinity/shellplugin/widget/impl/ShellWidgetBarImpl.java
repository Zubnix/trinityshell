package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.foundation.api.render.binding.view.View;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shellplugin.widget.api.ShellWidgetBar;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetBarImpl extends ShellWidgetImpl implements ShellWidgetBar {

	private final View view;

	@Inject
	ShellWidgetBarImpl(	final EventBus eventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						final PainterFactory painterFactory,
						final View view) {

		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		this.view = view;
	}

	@Override
	@ViewReference
	public View getView() {
		return this.view;
	}
}
