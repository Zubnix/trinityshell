package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@ThreadSafe
@OwnerThread("WindowManager")
public class XWmName extends XProtocolListenable {

	private final Map<Integer, String> protocolsByXWindowCache = new HashMap<Integer, String>();

	private final XConnection xConnection;
	private final ListeningExecutorService wmExecutor;

	@Inject
	XWmName(final XConnection xConnection,
			final XAtomCache xAtomCache,
			@Named("WindowManager") final ListeningExecutorService wmExecutor) {
		super(wmExecutor);

		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;
	}

	public ListenableFuture<String> getWmName(final DisplaySurface xWindow) {
		return this.wmExecutor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				final String name = XWmName.this.protocolsByXWindowCache.get(xWindow);
				return null;
			}
		});
	}
}