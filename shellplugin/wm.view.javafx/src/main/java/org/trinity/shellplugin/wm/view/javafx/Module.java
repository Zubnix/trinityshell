package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.render.ViewBuilder;
import org.trinity.foundation.render.javafx.api.FXViewBuilder;
import org.trinity.shellplugin.wm.api.viewkey.DesktopView;

import java.util.concurrent.CountDownLatch;

@GuiceModule
public class Module extends Application implements com.google.inject.Module {

    private static final CountDownLatch START_FX_APP_LATCH = new CountDownLatch(1);
    private static Binder GUICE_BINDER;
    private static Thread FX_APP_THREAD;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        GUICE_BINDER.bind(Stage.class).annotatedWith(Names.named("Primary")).toInstance(primaryStage);
        START_FX_APP_LATCH.countDown();
    }

    @Override
    public void configure(final Binder guiceBinder) {
        GUICE_BINDER = guiceBinder;
        startFxApp();

        FXViewBuilder desktopViewBuilder = new FXViewBuilder(DesktopViewImpl.class);
        guiceBinder.requestInjection(desktopViewBuilder);
        guiceBinder.bind(ViewBuilder.class).annotatedWith(DesktopView.class).toInstance(desktopViewBuilder);
    }

    private void startFxApp() {
        if(FX_APP_THREAD == null) {
            FX_APP_THREAD = new Thread("fx-application-starter") {
                @Override
                public void run() {
                    try {
                        Application.launch(Module.class);
                    } catch(Throwable t) {
                        t.printStackTrace();
                    }
                }
            };
            FX_APP_THREAD.start();
        }

        try {
            START_FX_APP_LATCH.await();
        } catch(InterruptedException e) {
            throw new Error(e);
        }
    }
}
