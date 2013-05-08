package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.trinity.foundation.api.display.DisplaySurface;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

public class XProtocolListenable {

	private final Map<DisplaySurface, List<XProtocolListener>> listenersByWindow = new HashMap<DisplaySurface, List<XProtocolListener>>();

	private final ListeningExecutorService wmExecutor;

	XProtocolListenable(final ListeningExecutorService wmExecutor) {
		this.wmExecutor = wmExecutor;
	}

	public ListenableFuture<Void> addProtocolListener(	final DisplaySurface xWindow,
														final XProtocolListener listener) {
		return this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				List<XProtocolListener> listeners = XProtocolListenable.this.listenersByWindow.get(xWindow);
				if (listeners == null) {
					listeners = new ArrayList<XProtocolListener>();
				}
				listeners.add(listener);
				return null;
			}
		});
	}

	public ListenableFuture<Void> removeProtocolListener(	final DisplaySurface xWindow,
															final XProtocolListener listener) {
		return this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				final List<XProtocolListener> listeners = XProtocolListenable.this.listenersByWindow.get(xWindow);
				listeners.remove(listener);
				return null;
			}
		});
	}

	protected void notifyProtocolListeners(final DisplaySurface xWindow) {
		final List<XProtocolListener> listeners = XProtocolListenable.this.listenersByWindow.get(xWindow);
		for (final XProtocolListener xProtocolListener : listeners) {
			xProtocolListener.onXProtocolChanged();
		}
	}
}