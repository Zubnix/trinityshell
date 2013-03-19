package org.trinity.shellplugin.wm.impl;

import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.devsurf.injection.guice.annotations.GuiceModule;

@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(ListeningExecutorService.class).annotatedWith(Names.named("WmExecutor"))
				.toInstance(MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor()));
	}
}