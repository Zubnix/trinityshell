package org.trinity.foundation.api.shared;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.MoreExecutors;

@ThreadSafe
public class AsyncListenableEventBus extends EventBus implements AsyncListenable {

	private final Map<Object, AsyncEventBus> asyncEventBusByListener = new WeakHashMap<Object, AsyncEventBus>();
	private final ExecutorService listenableExecutorService;

	public AsyncListenableEventBus(final ExecutorService postingExecutorService) {
		this.listenableExecutorService = postingExecutorService;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @see EventBus#register(Object)
	 */
	@Override
	public void register(final Object object) {
		register(	object,
					MoreExecutors.sameThreadExecutor());
	}

	@Override
	public void register(	final Object object,
							final ExecutorService executor) {
		this.listenableExecutorService.submit(new Callable<Void>() {
			@Override
			public Void call() {
				final AsyncEventBus asyncEventBus = new AsyncEventBus(executor);
				asyncEventBus.register(object);
				AsyncListenableEventBus.this.asyncEventBusByListener.put(	object,
																			asyncEventBus);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @see EventBus#unregister(Object)
	 */
	@Override
	public void unregister(final Object object) {
		this.listenableExecutorService.submit(new Callable<Void>() {
			@Override
			public Void call() {
				AsyncListenableEventBus.this.asyncEventBusByListener.remove(object);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @see EventBus#post(Object)
	 */
	@Override
	public void post(final Object event) {
		this.listenableExecutorService.submit(new Callable<Void>() {
			@Override
			public Void call() {
				for (final AsyncEventBus asyncEventBus : AsyncListenableEventBus.this.asyncEventBusByListener.values()) {
					asyncEventBus.post(event);
				}
				return null;
			}
		});
	}

}
