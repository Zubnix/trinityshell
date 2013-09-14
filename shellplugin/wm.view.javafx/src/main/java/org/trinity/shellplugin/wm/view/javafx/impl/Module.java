package org.trinity.shellplugin.wm.view.javafx.impl;

import javafx.application.Application;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.ViewReference;
import org.trinity.shellplugin.wm.api.viewreferencekey.DesktopViewReference;
import org.trinity.shellplugin.wm.view.javafx.api.FXModule;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.TypeLiteral;

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

	@Override
	protected void configure() {
		super.configure();
		bind(new TypeLiteral<ListenableFuture<ViewReference>>() {
		}).annotatedWith(DesktopViewReference.class).toProvider(DesktopViewReferenceProvider.class);
	}
}
