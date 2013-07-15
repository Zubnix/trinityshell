package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_get_property;
import static org.freedesktop.xcb.LibXcb.xcb_get_property_reply;
import static org.freedesktop.xcb.LibXcb.xcb_get_property_value;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;

import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_get_property_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.name.Named;

import javax.annotation.concurrent.NotThreadSafe;

@Bind
@To(IMPLEMENTATION)
@Singleton
@NotThreadSafe
@ExecutionContext(DisplayExecutor.class)
public class WmState extends AbstractCachedProtocol<int[]> {

	private static final Logger logger = LoggerFactory.getLogger(WmState.class);
	private final ListeningExecutorService wmExecutor;
	private final XConnection xConnection;

	WmState(@DisplayExecutor final ListeningExecutorService wmExecutor,
			final XConnection xConnection,
			final XAtomCache xAtomCache) {
		super(	wmExecutor,
				xAtomCache,
				"WM_STATE");
		this.wmExecutor = wmExecutor;
		this.xConnection = xConnection;
	}

	@Override
	protected ListenableFuture<Optional<int[]>> queryProtocol(final DisplaySurface xWindow) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_wm_state_cookie = xcb_get_property(	this.xConnection.getConnectionReference(),
																				(short) 0,
																				winId.intValue(),
																				getProtocolAtomId(),
																				getProtocolAtomId(),
																				0,
																				2);
		return this.wmExecutor.submit(new Callable<Optional<int[]>>() {

			@Override
			public Optional<int[]> call() {
				final xcb_generic_error_t e = new xcb_generic_error_t();
				final int[] reply = new int[2];

				final xcb_get_property_reply_t get_wm_state_reply = xcb_get_property_reply(	WmState.this.xConnection
																									.getConnectionReference(),
																							get_wm_state_cookie,
																							e);
				if (xcb_generic_error_t.getCPtr(e) != 0) {
					final String errorString = XcbErrorUtil.toString(e);
					logger.error(errorString);
					return Optional.absent();
				}
				if (get_wm_state_reply.getLength() == 0) {
					return Optional.absent();
				}
				final ByteBuffer wm_state_property_value = xcb_get_property_value(get_wm_state_reply).order(ByteOrder
						.nativeOrder());
				reply[0] = wm_state_property_value.getInt();
				reply[1] = wm_state_property_value.getInt();

				return Optional.of(reply);
			}
		});
	}

}
