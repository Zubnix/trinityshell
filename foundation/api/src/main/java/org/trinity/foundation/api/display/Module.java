package org.trinity.foundation.api.display;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;

@GuiceModule
public class Module extends AbstractModule {
	@Override
	protected void configure() {
		bind(ListeningExecutorService.class).annotatedWith(DisplayExecutor.class)
				.toInstance(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(final Runnable executorRunnable) {
						return new Thread(	executorRunnable,
											"display-executor");
					}
				})));
	}
}
