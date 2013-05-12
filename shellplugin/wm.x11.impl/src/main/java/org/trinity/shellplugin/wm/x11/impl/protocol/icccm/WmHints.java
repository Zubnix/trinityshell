package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_hints;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_hints_reply;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_wm_hints_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.XcbErrorUtil;
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
@OwnerThread("WindowManager")
@NotThreadSafe
public class WmHints extends AbstractCachedProtocol<xcb_icccm_wm_hints_t> {

	private static final Logger logger = LoggerFactory.getLogger(WmHints.class);

	private final XConnection xConnection;
	private final ListeningExecutorService wmExecutor;

	@Inject
	WmHints(@Named("WindowManager") final ListeningExecutorService wmExecutor,
			final XConnection xConnection,
			final XAtomCache xAtomCache,
			final String protocolName) {
		super(	wmExecutor,
				xAtomCache,
				"WM_HINTS");
		this.wmExecutor = wmExecutor;
		this.xConnection = xConnection;
	}

	@Override
	protected ListenableFuture<Optional<xcb_icccm_wm_hints_t>> queryProtocol(final DisplaySurface xWindow) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_wm_hints_cookie = xcb_icccm_get_wm_hints(	this.xConnection.getConnectionRef(),
																						winId.intValue());

		return this.wmExecutor.submit(new Callable<Optional<xcb_icccm_wm_hints_t>>() {
			@Override
			public Optional<xcb_icccm_wm_hints_t> call() throws Exception {
				final xcb_icccm_wm_hints_t hints = new xcb_icccm_wm_hints_t();
				final xcb_generic_error_t e = new xcb_generic_error_t();

				final short stat = xcb_icccm_get_wm_hints_reply(WmHints.this.xConnection.getConnectionRef(),
																get_wm_hints_cookie,
																hints,
																e);
				if (xcb_generic_error_t.getCPtr(e) != 0) {
					final String errorString = XcbErrorUtil.toString(e);
					logger.error(errorString);
					return Optional.absent();
				}

				if (stat == 0) {
					logger.error(	"Failed to read wm_hints reply from client={}",
									winId);
					return Optional.absent();
				}

				return Optional.of(hints);
			}
		});
	}
}