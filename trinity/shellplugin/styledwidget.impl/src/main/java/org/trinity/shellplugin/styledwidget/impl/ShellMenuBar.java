package org.trinity.shellplugin.styledwidget.impl;

import javax.inject.Named;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shellplugin.styledwidget.api.BaseShellWidgetStyled;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class ShellMenuBar extends BaseShellWidgetStyled {

	@Inject
	ShellMenuBar(	final EventBus eventBus,
					final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
					final PainterFactory painterFactory,
					@Named("ShellMenuBarView") final Object view) {

		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		setName("ShellMenuBar");
	}
}
