package org.trinity.foundation.render.javafx.api;

import javafx.application.Application;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractApplication extends Application {

    private static final CountDownLatch INSTANCE_LATCH = new CountDownLatch(1);
    private static AbstractApplication INSTANCE;

    @Override
    public void init() {
        INSTANCE = this;
        INSTANCE_LATCH.countDown();
    }

    public static AbstractApplication GET() throws InterruptedException {
        INSTANCE_LATCH.await();
        return INSTANCE;
    }
}
