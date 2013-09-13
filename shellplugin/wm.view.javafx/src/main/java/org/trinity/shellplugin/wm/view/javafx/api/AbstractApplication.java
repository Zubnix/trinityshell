package org.trinity.shellplugin.wm.view.javafx.api;

import java.util.concurrent.CountDownLatch;

import javafx.application.Application;

public abstract class AbstractApplication extends Application {

	private static final CountDownLatch instanceLatch = new CountDownLatch(1);
	private static AbstractApplication instance;

	@Override
	public void init(){
		instance = this;
		instanceLatch.countDown();
	}

	public static AbstractApplication GET() throws InterruptedException {
		instanceLatch.await();
		return instance;
	}
}
