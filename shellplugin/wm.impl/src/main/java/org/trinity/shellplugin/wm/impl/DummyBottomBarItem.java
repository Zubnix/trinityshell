package org.trinity.shellplugin.wm.impl;

import org.trinity.shellplugin.wm.api.BottomBarItem;
import org.trinity.shellplugin.wm.api.HasText;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class DummyBottomBarItem implements HasText, BottomBarItem {

	@Override
	public String getText() {
		return "static bottom item";
	}

}
