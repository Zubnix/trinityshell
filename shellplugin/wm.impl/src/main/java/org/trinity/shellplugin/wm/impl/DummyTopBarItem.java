package org.trinity.shellplugin.wm.impl;

import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.TopBarItem;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class DummyTopBarItem implements HasText, TopBarItem {

	@Override
	public String getText() {
		return "static top item";
	}
}