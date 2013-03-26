package org.trinity.shellplugin.wm.impl;

import org.trinity.shellplugin.wm.api.TopBarItem;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class DummyTopBarItem implements TopBarItem {

	@Override
	public String getText() {
		return "dummy item";
	}
}