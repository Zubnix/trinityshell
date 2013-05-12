package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_protocols;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_protocols_reply;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_wm_protocols_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
@NotThreadSafe
@OwnerThread("WindowManager")
public class WmProtocols extends AbstractCachedProtocol<xcb_icccm_get_wm_protocols_reply_t> {

	private static final Logger logger = LoggerFactory.getLogger(WmProtocols.class);

	private final XConnection xConnection;
	private final ListeningExecutorService wmExecutor;

	@Inject
	WmProtocols(final XConnection xConnection,
				final XAtomCache xAtomCache,
				@Named("WindowManager") final ListeningExecutorService wmExecutor) {
		super(	wmExecutor,
				xAtomCache,
				"WM_PROTOCOLS");

		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;
	}

	@Override
	protected ListenableFuture<Optional<xcb_icccm_get_wm_protocols_reply_t>> queryProtocol(final DisplaySurface xWindow) {
		final int window = ((Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle()).intValue();
		final xcb_get_property_cookie_t get_property_cookie = xcb_icccm_get_wm_protocols(	this.xConnection.getConnectionRef(),
																							window,
																							getProtocolAtomId());
		return this.wmExecutor.submit(new Callable<Optional<xcb_icccm_get_wm_protocols_reply_t>>() {
			@Override
			public Optional<xcb_icccm_get_wm_protocols_reply_t> call() {

				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_icccm_get_wm_protocols_reply_t wm_protocols = new xcb_icccm_get_wm_protocols_reply_t();
				final short stat = xcb_icccm_get_wm_protocols_reply(WmProtocols.this.xConnection.getConnectionRef(),
																	get_property_cookie,
																	wm_protocols,
																	e);
				if ((stat == 0) || (xcb_generic_error_t.getCPtr(e) != 0)) {
					logger.error(	"Failed to get wm_protocols property from window={}",
									window);
					return Optional.absent();
				}

				return Optional.of(wm_protocols);
			}
		});
	}
}