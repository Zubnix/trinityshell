package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ShellRootWidget extends BaseShellWidget {

	@Inject
	@Named("RootView")
	private Object view;

	@Inject
	protected ShellRootWidget(	final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory) {
		super(	shellDisplayEventDispatcher,
				painterFactory);
	}

	@ViewReference
	public Object getView() {
		return this.view;
	}
}
