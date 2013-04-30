package org.trinity.shellplugin.wm.x11.impl.view;

import org.trinity.shellplugin.widget.impl.view.qt.AbstractQWidgetViewProvider;

public class RootViewProvider extends AbstractQWidgetViewProvider<RootView> {

	@Override
	protected RootView createView() {
		return new RootView();
	}
}