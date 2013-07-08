package org.trinity.shell.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.AbstractModule;
import com.google.inject.name.Named;

/***************************************
 * Registers useful objects in the Guice injection framework. The following
 * objects are registered:
 * <p>
 * <ul>
 * <li>An {@link EventBus} which is {@link Named} "ShellEventBus". This eventbus
 * is the primary means of communication between independent {@link ShellPlugin}
 * s and informs any subscribed listener to changes of the shell. This eventbus
 * is driven by a single shell thread, subscribers should thus not block their
 * handling of notifications.
 * </ul>
 *
 ***************************************
 */
@GuiceModule
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		ListeningExecutorService shellExecutor = MoreExecutors.listeningDecorator(Executors
				.newSingleThreadExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(final Runnable r) {
						return new Thread(	r,
											"shell-executor");
					}
				}));
		bind(ListeningExecutorService.class).annotatedWith(ShellExecutor.class).toInstance(shellExecutor);
		bind(AsyncListenable.class).annotatedWith(ShellScene.class)
				.toInstance(new AsyncListenableEventBus(shellExecutor));
	}
}
