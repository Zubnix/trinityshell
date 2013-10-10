/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.api.shared;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.MoreExecutors;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Extends Guava's {@link EventBus} with asynchronous event delivery per listener.
 */
@ThreadSafe
public class AsyncListenableEventBus extends EventBus implements AsyncListenable {

	private final Map<Object, AsyncEventBus> asyncEventBusByListener = new WeakHashMap<Object, AsyncEventBus>();
    Lock scheduledListenersLock = new ReentrantLock(true);
    private Deque<Callable> scheduledListeners = new LinkedList<>();
    private final ExecutorService listenableExecutorService;

    public AsyncListenableEventBus(@Nonnull final ExecutorService postingExecutorService) {
        this.listenableExecutorService = postingExecutorService;
    }

    /**
     * {@inheritDoc}
     * <p>
	 *
	 * @see EventBus#register(Object)
	 */
	@Override
	public void register(@Nonnull final Object object) {
		register(	object,
					MoreExecutors.sameThreadExecutor());
	}

	@Override
	public void register(	@Nonnull final Object object,
							@Nonnull final ExecutorService executor) {
        //schedule the listener for inclusion when the next event notification starts.
        Callable<Void> scheduledListener = new Callable<Void>() {
            @Override
            public Void call() {
                final AsyncEventBus asyncEventBus = new AsyncEventBus(executor);
                asyncEventBus.register(object);
                AsyncListenableEventBus.this.asyncEventBusByListener.put(object,
                                                                         asyncEventBus);
                return null;
            }
        };
        scheduledListenersLock.lock();
        try {
            scheduledListeners.add(scheduledListener);
        } finally {
            scheduledListenersLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * @see EventBus#unregister(Object)
     */
	@Override
	public void unregister(@Nonnull final Object object) {
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
	public void post(@Nonnull final Object event) {
		this.listenableExecutorService.submit(new Callable<Void>() {
			@Override
            public Void call() throws Exception {
                addScheduledListeners();

                for(final AsyncEventBus asyncEventBus : AsyncListenableEventBus.this.asyncEventBusByListener.values()) {
                    asyncEventBus.post(event);
                }
                return null;
            }
        });
    }

    private void addScheduledListeners() throws Exception {
        scheduledListenersLock.lock();
        try {
            for(Callable<Void> scheduledListener : scheduledListeners) {
                scheduledListener.call();
            }
        } finally {
            scheduledListenersLock.unlock();
        }
    }

}
