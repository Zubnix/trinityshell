package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.trinity.foundation.api.render.binding.ViewBinder;
import org.trinity.foundation.render.javafx.api.FXView;


public class DesktopViewImpl extends FXView {

    @Inject
    DesktopViewImpl(@Named("Primary") Stage primaryStage,

                    final ViewBinder viewBinder) {
        super(
              viewBinder);
        getStyleClass().add("desktop-view");

        final Rectangle2D r = Screen.getPrimary().getBounds();
        final Scene scene = new Scene(this,
                                      r.getWidth(),
                                      r.getHeight());
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.setWidth(r.getWidth());
        primaryStage.setHeight(r.getHeight());
        primaryStage.show();
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }
}
