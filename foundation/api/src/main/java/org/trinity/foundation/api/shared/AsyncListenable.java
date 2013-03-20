package org.trinity.foundation.api.shared;

import java.util.concurrent.ExecutorService;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;

public interface AsyncListenable {
	/**
	 * Register a listener who's {@link Subscribe}d methods will be invoked by
	 * the {@link MoreExecutors#sameThreadExecutor()}.
	 * 
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 */
	void register(Object listener);

	/**
	 * Register a listener who's {@link Subscribe}d methods will be invoked by
	 * the given {@link ExecutorService}.
	 * 
	 * @see {@link EventBus#register(Object)}
	 * @param listener
	 * @param executor
	 */
	void register(	Object listener,
					ExecutorService executor);

	/**
	 * @see {@link EventBus#unregister(Object)}
	 * @param listener
	 */
	void unregister(Object listener);

	/**
	 * Asynchronously post an event.
	 * 
	 * @see {@link EventBus#post(Object)}
	 * @param event
	 */
	void post(Object event);
}
