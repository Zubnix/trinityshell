package org.trinity.foundation.render.javafx.api;

import javafx.application.Application;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractFXApplication extends Application {

    private static final CountDownLatch INSTANCE_LATCH = new CountDownLatch(1);
    private static AbstractFXApplication INSTANCE;

    @Override
    public void init() {
        INSTANCE = this;
        INSTANCE_LATCH.countDown();
    }

    public static AbstractFXApplication GET() throws InterruptedException {
        INSTANCE_LATCH.await();
        return INSTANCE;
    }
}
