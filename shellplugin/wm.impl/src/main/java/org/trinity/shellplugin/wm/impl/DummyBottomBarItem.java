package org.trinity.shellplugin.wm.impl;

import org.trinity.shellplugin.wm.api.BottomBarItem;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class DummyBottomBarItem implements BottomBarItem {

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
