package org.trinity.shellplugin.wm.view.javafx;

import com.google.inject.Binder;
import com.google.inject.name.Names;
import dagger.Module;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

@Module
public class JFXViewViewPluginImpl {

    private static final CountDownLatch START_FX_APP_LATCH = new CountDownLatch(1);
    private static Binder GUICE_BINDER;
    private static Thread FX_APP_THREAD;

    //TODO
    public void start(final Stage primaryStage) throws Exception {
        GUICE_BINDER.bind(Stage.class).annotatedWith(Names.named("Primary")).toInstance(primaryStage);
        START_FX_APP_LATCH.countDown();
    }

	//TODO
    public void configure(final Binder guiceBinder) {
        GUICE_BINDER = guiceBinder;
        startFxApp();
    }

    private void startFxApp() {
        if(FX_APP_THREAD == null) {
            FX_APP_THREAD = new Thread("fx-application-starter") {
                @Override
                public void run() {
                    try {
						//TODO
                        //Application.launch(JFXViewViewPluginImpl.class);
                    } catch(final Throwable t) {
                        t.printStackTrace();
                    }
                }
            };
            FX_APP_THREAD.start();
        }

        try {
            START_FX_APP_LATCH.await();
        } catch(final InterruptedException e) {
            throw new Error(e);
        }
    }
}
