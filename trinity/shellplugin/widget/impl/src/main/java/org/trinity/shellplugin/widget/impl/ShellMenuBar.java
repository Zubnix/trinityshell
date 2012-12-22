package org.trinity.shellplugin.widget.impl;

import javax.inject.Named;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shellplugin.widget.api.BaseShellWidgetStyled;

import com.google.common.eventbus.EventBus;

public class ShellMenuBar extends BaseShellWidgetStyled {

	public ShellMenuBar(EventBus eventBus,
						ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						PainterFactory painterFactory,
						@Named("StyledQJView") Object view) {

		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
		setName("ShellMenuBar");
	}

}
