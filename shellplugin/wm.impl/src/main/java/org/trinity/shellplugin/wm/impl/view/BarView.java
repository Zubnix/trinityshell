package org.trinity.shellplugin.wm.impl.view;

import com.trolltech.qt.gui.QFrame;

class BarView extends QFrame {
	BarView(final RootView rootView) {
		super(rootView);
	}

	{
		// workaround for jambi css bug
		setObjectName(getClass().getSimpleName());
	}
}