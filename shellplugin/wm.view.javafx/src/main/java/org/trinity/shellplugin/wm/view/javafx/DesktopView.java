package org.trinity.shellplugin.wm.view.javafx;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.CountDownLatch;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind(to = @To(IMPLEMENTATION))
@Singleton
public class DesktopView extends FXView {

    private static DesktopView DESKTOPVIEW_HACK;
    private static final CountDownLatch STARTUP_HACK = new CountDownLatch(1);


    @Inject
    DesktopView() throws InterruptedException {
        getStyleClass().add("desktop-view");

        DESKTOPVIEW_HACK = this;
        new Thread() {
            @Override
            public void run() {
                Application.launch(FXApplication.class);
            }
        }.start();

        STARTUP_HACK.await();
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/org/trinity/shellplugin/wm/view/javafx/skin/skin.css").toExternalForm();
    }

    /**
     *
     */
    public static class FXApplication extends Application {

        public FXApplication() {
        }

        @Override
        public void start(final Stage stage) throws Exception {
            final Rectangle2D r = Screen.getPrimary().getBounds();
            final Scene scene = new Scene(DESKTOPVIEW_HACK, r.getWidth(), r.getHeight());
            stage.setScene(scene);

            stage.setResizable(false);
            stage.setWidth(r.getWidth());
            stage.setHeight(r.getHeight());
            stage.show();

            STARTUP_HACK.countDown();
        }
    }
}
