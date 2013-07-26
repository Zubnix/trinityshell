package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public abstract class AbstractCachedProtocol<P> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCachedProtocol.class);
    private final Map<DisplaySurface, Optional<P>> protocolCache = new WeakHashMap<DisplaySurface, Optional<P>>();
    private final Map<DisplaySurface, EventBus> listenersByWindow = new WeakHashMap<DisplaySurface, EventBus>();
    private final ListeningExecutorService displayExecutor;
    private int protocolAtomId;

    // TODO use display execution context
    AbstractCachedProtocol(@Nonnull @DisplayExecutor final ListeningExecutorService displayExecutor,
                           @Nonnull final XAtomCache xAtomCache,
                           @Nonnull final String protocolName) {
        this.displayExecutor = displayExecutor;
        displayExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                AbstractCachedProtocol.this.protocolAtomId = xAtomCache.getAtom(protocolName);
                return null;
            }
        });
    }

    public int getProtocolAtomId() {
        return this.protocolAtomId;
    }

    public void addProtocolListener(final DisplaySurface xWindow,
                                    final ProtocolListener<P> listener) {

        EventBus listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
        if (listeners == null) {
            listeners = new EventBus();
            this.listenersByWindow.put(xWindow,
                    listeners);
        }
        listeners.register(listener);
    }

    public ListenableFuture<Optional<P>> get(final DisplaySurface xWindow) {

        final ListenableFuture<Optional<P>> protocolFuture = this.displayExecutor.submit(new Callable<Optional<P>>() {
            @Override
            public Optional<P> call() throws Exception {
                return AbstractCachedProtocol.this.protocolCache.get(xWindow);
            }
        });

        return transform(protocolFuture,
                new AsyncFunction<Optional<P>, Optional<P>>() {
                    @Override
                    public ListenableFuture<Optional<P>> apply(final Optional<P> protocol) {
                        if (protocol == null) {
                            trackProtocol(xWindow);
                            return queryProtocol(xWindow);
                        }
                        return MoreExecutors.sameThreadExecutor().submit(new Callable<Optional<P>>() {
                            @Override
                            public Optional<P> call() {
                                return protocol;
                            }
                        });
                    }
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
                        AbstractCachedProtocol.this.protocolCache.put(xWindow,
                                protocol);
                        notifyProtocolListeners(xWindow,
                                protocol);
                    }

                    @Override
                    public void onFailure(final Throwable t) {
                        LOG.error("Failed to update protocol.",
                                t);
                    }
                });
    }

    protected abstract ListenableFuture<Optional<P>> queryProtocol(final DisplaySurface xWindow);

    public void removeProtocolListener(final DisplaySurface xWindow,
                                       final ProtocolListener<P> listener) {

        final EventBus listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
        listeners.unregister(listener);
    }

    protected void notifyProtocolListeners(final DisplaySurface xWindow,
                                           final Optional<P> protocol) {
        final EventBus listeners = AbstractCachedProtocol.this.listenersByWindow.get(xWindow);
        listeners.post(protocol);
    }
}
