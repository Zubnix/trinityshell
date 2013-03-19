package org.trinity.shellplugin.wm.impl.view;

import org.trinity.shellplugin.widget.impl.view.qt.AbstractQWidgetViewProvider;

public class ShellRootViewProvider extends AbstractQWidgetViewProvider<RootView> {

	@Override
	protected RootView createView() {
		return new RootView();
	}
}