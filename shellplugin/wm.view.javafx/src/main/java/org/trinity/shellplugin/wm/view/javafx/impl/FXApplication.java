package org.trinity.shellplugin.wm.view.javafx.impl;


import com.cathive.fx.guice.GuiceApplication;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.inject.Inject;

public abstract class FXApplication extends GuiceApplication {

    @Inject
    private FXDesktopView desktop;

    @Override
    public void start(final Stage stage) throws Exception {
        final Rectangle2D r = Screen.getPrimary().getBounds();
        final Scene scene = new Scene(desktop, r.getWidth(), r.getHeight());
        stage.setResizable(false);
        stage.setWidth(r.getWidth());
        stage.setHeight(r.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
