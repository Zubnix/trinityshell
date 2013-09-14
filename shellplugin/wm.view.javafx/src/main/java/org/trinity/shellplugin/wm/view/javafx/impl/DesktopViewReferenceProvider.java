package org.trinity.shellplugin.wm.view.javafx.impl;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import javafx.application.Platform;

import javafx.scene.Node;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.render.ViewReference;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Provider;

public class DesktopViewReferenceProvider implements Provider<ListenableFuture<ViewReference>> {

	private final GuiceFXMLLoader fxmlLoader;

	@Inject
	DesktopViewReferenceProvider(GuiceFXMLLoader fxmlLoader) {
		this.fxmlLoader = fxmlLoader;
	}

	@Override
	public ListenableFuture<ViewReference> get() {

		ListenableFutureTask<ViewReference> task = ListenableFutureTask.create(new Callable<ViewReference>() {
			@Override
			public ViewReference call() throws Exception {
				return constructViewReference();
			}
		});
		Platform.runLater(task);
		return task;
	}

	private ViewReference constructViewReference() throws IOException {
		GuiceFXMLLoader.Result desktopNodeResult = fxmlLoader.load(getClass().getResource("Desktop.fxml"));
		desktopNodeResult.getController();

		return new ViewReference() {
			@Override
			public Object getView() {
				return null;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public DisplaySurface getViewDisplaySurface() {
				return null;  //To change body of implemented methods use File | Settings | File Templates.
			}
		};
	}
}
