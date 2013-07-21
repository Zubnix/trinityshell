package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name_reply;

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

@Bind(to=@To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public class WmName extends AbstractCachedProtocol<xcb_icccm_get_text_property_reply_t> {

	private static final Logger LOG = LoggerFactory.getLogger(WmName.class);
	private final XConnection xConnection;
	private final ListeningExecutorService displayExecutor;

	@Inject
	WmName(	final XConnection xConnection,
			final XAtomCache xAtomCache,
			@DisplayExecutor final ListeningExecutorService displayExecutor) {
		super(	displayExecutor,
				xAtomCache,
				"WM_NAME");

		this.xConnection = xConnection;
		this.displayExecutor = displayExecutor;
	}

	@Override
	protected ListenableFuture<Optional<xcb_icccm_get_text_property_reply_t>> queryProtocol(final DisplaySurface xWindow) {
		final int window = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_property_cookie = xcb_icccm_get_wm_name(this.xConnection.getConnectionReference(),
																					window);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_icccm_get_text_property_reply_t prop = new xcb_icccm_get_text_property_reply_t();

		return this.displayExecutor.submit(new Callable<Optional<xcb_icccm_get_text_property_reply_t>>() {
			@Override
			public Optional<xcb_icccm_get_text_property_reply_t> call() throws Exception {

				final short stat = xcb_icccm_get_wm_name_reply(	WmName.this.xConnection.getConnectionReference(),
																get_property_cookie,
																prop,
																e);
				if (stat == 0) {
					LOG.error("Error retrieving wm_name reply from client={}",
							window);
					return Optional.absent();
				}

				return Optional.of(prop);
			}
		});
	}
}
