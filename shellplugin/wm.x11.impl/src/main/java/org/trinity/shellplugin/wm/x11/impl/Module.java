package org.trinity.shellplugin.wm.x11.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ListeningExecutorService.class).annotatedWith(Names.named("WindowManager"))
				.toInstance(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new ThreadFactory() {

					@Override
					public Thread newThread(final Runnable r) {
						return new Thread(	r,
											"wm-executor");
					}
				})));
	}
}