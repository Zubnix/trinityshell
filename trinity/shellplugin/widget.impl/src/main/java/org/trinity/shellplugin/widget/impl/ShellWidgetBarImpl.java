package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.view.View;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shellplugin.widget.api.ShellWidgetBar;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetBarImpl extends ShellWidgetImpl implements
		ShellWidgetBar {

	@Inject
	ShellWidgetBarImpl(	final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						final PainterFactory painterFactory,
						final View view) {

		super(	shellDisplayEventDispatcher,
				painterFactory,
				view);
	}
}
