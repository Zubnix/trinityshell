package org.trinity.shellplugin.styledwidget.view.qt.impl;

import javax.inject.Named;

import org.trinity.foundation.api.render.binding.view.PropertySlot;

import com.trolltech.qt.gui.QFrame;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
// @To(value = Type.CUSTOM, customs = Object.class)
@Named("ShellMenuBarView")
public class ShellMenuBarView extends QFrame {

	ShellMenuBarView() {
	}

	@PropertySlot("name")
	public void setName(final String name) {
		setObjectName(name);
	}

}