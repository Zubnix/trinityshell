package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;
import org.trinity.shellplugin.wm.x11.impl.protocol.XPropertyChanged;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@NotThreadSafe
@OwnerThread("WindowManager")
public abstract class AbstractProtocolCache<P> {

	private static Logger logger = LoggerFactory.getLogger(AbstractProtocolCache.class);

	private final Map<DisplaySurface, Optional<P>> protocolCache = new WeakHashMap<DisplaySurface, Optional<P>>();
	private final Map<DisplaySurface, List<ProtocolListener>> listenersByWindow = new WeakHashMap<DisplaySurface, List<ProtocolListener>>();

	private final ListeningExecutorService wmExecutor;

	private int protocolAtomId;

	AbstractProtocolCache(	final ListeningExecutorService wmExecutor,
							final XAtomCache xAtomCache,
							final String protocolName) {
		this.wmExecutor = wmExecutor;
		addCallback(xAtomCache.internAtom(protocolName),
					new FutureCallback<Integer>() {
						@Override
						public void onSuccess(final Integer result) {
							AbstractProtocolCache.this.protocolAtomId = result.intValue();
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to intern atom.",
											t);
						}
					});
	}

	protected int getProtocolAtomId() {
		return this.protocolAtomId;
	}

	public void addProtocolListener(final DisplaySurface xWindow,
									final ProtocolListener listener) {

		List<ProtocolListener> listeners = AbstractProtocolCache.this.listenersByWindow.get(xWindow);
		if (listeners == null) {
			listeners = new ArrayList<ProtocolListener>();
		}
		listeners.add(listener);
	}

	public ListenableFuture<Optional<P>> get(final DisplaySurface xWindow) {

		final ListenableFuture<Optional<P>> protocolFuture = this.wmExecutor.submit(new Callable<Optional<P>>() {
			@Override
			public Optional<P> call() throws Exception {
				return AbstractProtocolCache.this.protocolCache.get(xWindow);
			}
		});

		return transform(	protocolFuture,
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
		xWindow.register(new XPropertyChanged() {

			@Override
			@Subscribe
			public void onXPropertyChanged(final xcb_property_notify_event_t property_notify_event) {
				if (property_notify_event.getAtom() == AbstractProtocolCache.this.protocolAtomId) {
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
							AbstractProtocolCache.this.protocolCache.put(	xWindow,
																			protocol);
							notifyProtocolListeners(xWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to update protocol.",
											t);
						}
					});
	}

	protected abstract ListenableFuture<Optional<P>> queryProtocol(final DisplaySurface xWindow);

	public void removeProtocolListener(	final DisplaySurface xWindow,
										final ProtocolListener listener) {

		final List<ProtocolListener> listeners = AbstractProtocolCache.this.listenersByWindow.get(xWindow);
		listeners.remove(listener);
	}

	protected void notifyProtocolListeners(final DisplaySurface xWindow) {
		final List<ProtocolListener> listeners = AbstractProtocolCache.this.listenersByWindow.get(xWindow);
		for (final ProtocolListener protocolListener : listeners) {
			protocolListener.onProtocolChanged();
		}
	}
}