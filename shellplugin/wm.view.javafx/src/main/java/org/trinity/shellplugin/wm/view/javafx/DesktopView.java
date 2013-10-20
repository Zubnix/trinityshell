package org.trinity.shellplugin.wm.view.javafx;

import com.google.common.util.concurrent.ListeningExecutorService;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Skin;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.render.binding.Binder;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.foundation.render.javafx.api.FXView;
import org.trinity.foundation.render.javafx.api.FXViewBuilder;
import org.trinity.shell.api.bindingkey.ShellExecutor;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;


@Bind(to = @To(IMPLEMENTATION))
public class DesktopView extends FXView {

    //HACK HACK HACK since JavaFX has a *special* way of initializing an application, we can't inject it
    //with Guice. So we have to come up with a hack.
    private static DesktopView DESKTOPVIEW_HACK;
    private static final CountDownLatch STARTUP_HACK = new CountDownLatch(1);

    @Inject
    DesktopView(final @ShellExecutor ListeningExecutorService shellExecutor,
                final Binder binder) throws InterruptedException {
        super(shellExecutor,
              binder);
        getStyleClass().add("desktop-view");

        DESKTOPVIEW_HACK = this;
        new Thread("fx-application-starter") {
            @Override
            public void run() {
                Application.launch(FXApplication.class);
            }
        }.start();
        STARTUP_HACK.await();

    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/skin.css").toExternalForm();
    }

    /**
     *
     */
    public static class FXApplication extends Application {

        @Override
        public void start(final Stage stage) throws Exception {

            final Rectangle2D r = Screen.getPrimary().getBounds();
            final Scene scene = new Scene(DESKTOPVIEW_HACK, r.getWidth(), r.getHeight());
            stage.setScene(scene);

            stage.setResizable(false);
            stage.setWidth(r.getWidth());
            stage.setHeight(r.getHeight());
            stage.show();

            FXViewBuilder.PLATFORM_STARTED_HACK = true;
            STARTUP_HACK.countDown();
        }
    }
}
