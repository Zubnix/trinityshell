package org.trinity.shellplugin.wm.view.javafx;


import com.cathive.fx.guice.GuiceApplication;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.Set;

import static com.google.inject.Stage.PRODUCTION;

public abstract class FXApplication extends GuiceApplication {

    @Inject
    private DesktopView desktop;
    @Inject
    private Injector injector;

    @Override
    public void start(final Stage stage) throws Exception {
        final Rectangle2D r = Screen.getPrimary().getBounds();
        final Scene scene = new Scene(desktop, r.getWidth(), r.getHeight());
        stage.setResizable(false);
        stage.setWidth(r.getWidth());
        stage.setHeight(r.getHeight());
        stage.setScene(scene);
        stage.show();

        postStart();
    }

    public void postStart() {

    }
}
