package org.trinity.shellplugin.styledwidget.impl;

import javax.inject.Named;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shellplugin.styledwidget.api.BaseShellWidgetStyled;

import com.google.common.eventbus.EventBus;

public class ShellMenuBar extends BaseShellWidgetStyled {

	public ShellMenuBar(final EventBus eventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						final PainterFactory painterFactory,
						@Named("StyledQJView") final Object view) {

		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		setName("ShellMenuBar");
	}

}
