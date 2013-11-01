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

package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@ThreadSafe
@ExecutionContext(DisplayExecutor.class)
public abstract class AbstractCachedProtocol<P> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCachedProtocol.class);
	private final Map<DisplaySurface, Optional<P>> protocolCache = new WeakHashMap<>();
	private final Map<DisplaySurface, AsyncListenable> listenersByWindow = new WeakHashMap<>();
	private final ListeningExecutorService displayExecutor;
	private int protocolAtomId;

	AbstractCachedProtocol(	@Nonnull @DisplayExecutor final ListeningExecutorService displayExecutor,
							@Nonnull final XAtomCache xAtomCache,
							@Nonnull final String protocolName) {
		this.displayExecutor = displayExecutor;
		displayExecutor.submit(() -> {
            this.protocolAtomId = xAtomCache.getAtom(protocolName);
            return null;
        });
	}

	public int getProtocolAtomId() {
		return this.protocolAtomId;
	}

	public ListenableFuture<Void> addProtocolListener(	@Nonnull final DisplaySurface xWindow,
														@Nonnull final ProtocolListener<P> listener,
														@Nonnull final ExecutorService executor) {
		return this.displayExecutor.submit(() -> {
            AsyncListenable listeners = this.listenersByWindow.get(xWindow);
            if (listeners == null) {
                listeners = new AsyncListenableEventBus(MoreExecutors.sameThreadExecutor());
                listenersByWindow.put(	xWindow,
                                        listeners);
            }
            listeners.register(	listener,
                                executor);
            return null;
        });
	}

	public ListenableFuture<Optional<P>> get(@Nonnull final DisplaySurface xWindow) {

		final ListenableFuture<Optional<P>> protocolFuture = this.displayExecutor.submit((Callable<Optional<P>>) () -> AbstractCachedProtocol.this.protocolCache.get(xWindow));

		return transform(	protocolFuture,
                             (AsyncFunction<Optional<P>, Optional<P>>) protocol -> {
                                 if (protocol == null) {
                                     trackProtocol(xWindow);
                                     return queryProtocol(xWindow);
                                 }
                                 return MoreExecutors.sameThreadExecutor().submit(() -> protocol);
                             });
	}

	protected void trackProtocol(final DisplaySurface xWindow) {
		xWindow.register(new Object() {
			@Subscribe
			public void onXPropertyChanged(final xcb_property_notify_event_t property_notify_event) {
				if (property_notify_event.getAtom() == AbstractCachedProtocol.this.protocolAtomId) {
					updateProtocolCache(xWindow);
				}
			}
		});
	}

	protected void updateProtocolCache(final DisplaySurface xWindow) {
		final ListenableFuture<Optional<P>> protocolFuture = queryProtocol(xWindow);
		addCallback(protocolFuture,
					new FutureCallback<Optional<P>>() {
						@Override
						public void onSuccess(final Optional<P> protocol) {
							AbstractCachedProtocol.this.protocolCache.put(	xWindow,
																			protocol);
							notifyProtocolListeners(xWindow,
													protocol);
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error(	"Failed to update protocol.",
										t);
						}
					});
	}

	protected abstract ListenableFuture<Optional<P>> queryProtocol(final DisplaySurface xWindow);

	public ListenableFuture<Void> removeProtocolListener(	@Nonnull final DisplaySurface xWindow,
															@Nonnull final ProtocolListener<P> listener) {
		return this.displayExecutor.submit(() -> {
            final AsyncListenable listeners = this.listenersByWindow.get(xWindow);
            listeners.unregister(listener);
            return null;
        });
	}

	protected void notifyProtocolListeners(	final DisplaySurface xWindow,
											final Optional<P> protocol) {
		final AsyncListenable listeners = this.listenersByWindow.get(xWindow);
		listeners.post(protocol);
	}
}
