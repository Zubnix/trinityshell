package org.trinity.foundation.render.javafx.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JFXApplication extends Application {

	private static final ExecutorService JFX_THREAD = Executors.newSingleThreadExecutor(new ThreadFactory() {

		@Override
		public Thread newThread(final Runnable r) {
			return new Thread(	r,
								"javafx-application");
		}
	});

	public static void start() {
		JFX_THREAD.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				launch();
				return null;
			}
		});
	}

	@Override
	public void start(final Stage stage) throws Exception {
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.TRANSPARENT);

		// f*cking "Press ESC to exit fullscreen" messsage...
		// stage.setFullScreen(true);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		// set Stage boundaries to visible bounds of the main screen
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
		// define a scene here?

		Scene scene = new Scene(new Group(),
								primaryScreenBounds.getWidth(),
								primaryScreenBounds.getHeight());
		stage.setScene(scene);

		stage.show();
	}
}
