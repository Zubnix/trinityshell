package org.trinity.shellplugin.wm.view.javafx.impl;

import javafx.application.Application;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.shellplugin.wm.view.javafx.api.FXModule;

@GuiceModule
public class Module extends FXModule {
	public Module() {
		super(new Runnable() {
			@Override
			public void run() {
				Application.launch(TrinityFXApplication.class);
			}
		});
	}
}
