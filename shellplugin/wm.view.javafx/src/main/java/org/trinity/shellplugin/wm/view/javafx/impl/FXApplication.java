package org.trinity.shellplugin.wm.view.javafx.impl;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.trinity.foundation.render.javafx.api.AbstractFXApplication;

import javax.inject.Inject;


public class FXApplication extends AbstractFXApplication {

    @Inject
    private Region desktop;

    @Override
    public void start(final Stage stage) throws Exception {
        final Rectangle2D r = Screen.getPrimary().getBounds();
        final Scene scene = new Scene(desktop, r.getWidth(), r.getHeight());
        stage.setResizable(false);
        stage.setWidth(r.getWidth());
        stage.setHeight(r.getHeight());
        stage.setScene(scene);
    }
}
