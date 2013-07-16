package org.trinity.foundation.display.x11.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;

@GuiceModule
public class Module extends AbstractModule {
	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(XEventBus.class).toInstance(new EventBus());
	}
}
