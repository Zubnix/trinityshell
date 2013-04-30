package org.trinity.shellplugin.wm.x11.impl;

import com.google.inject.AbstractModule;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		// bind(ListeningExecutorService.class).annotatedWith(Names.named("WmExecutor"))
		// .toInstance(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new
		// ThreadFactory() {
		//
		// @Override
		// public Thread newThread(final Runnable r) {
		// return new Thread( r,
		// "wm-executor");
		// }
		// })));
	}
}