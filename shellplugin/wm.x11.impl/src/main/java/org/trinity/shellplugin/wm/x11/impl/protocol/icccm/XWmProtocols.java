package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_wm_protocols_reply_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;
import org.trinity.shellplugin.wm.x11.impl.protocol.XPropertyChanged;

import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_protocols;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_protocols_reply;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
@ThreadSafe
@OwnerThread("WindowManager")
public class XWmProtocols extends XProtocolListenable {

	private static final Logger logger = LoggerFactory.getLogger(XWmProtocols.class);

	private int wmProtocolsAtomId;

	private final Map<Integer, Set<Integer>> protocolsByXWindowCache = new HashMap<Integer, Set<Integer>>();

	private final XConnection xConnection;
	private final ListeningExecutorService wmExecutor;

	@Inject
	XWmProtocols(	final XConnection xConnection,
					final XAtomCache xAtomCache,
					@Named("WindowManager") final ListeningExecutorService wmExecutor) {
		super(wmExecutor);
		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;

		final ListenableFuture<Integer> wmProtocolsFuture = xAtomCache.internAtom("WM_PROTOCOLS");
		addCallback(wmProtocolsFuture,
					new FutureCallback<Integer>() {
						@Override
						public void onSuccess(final Integer wmProtocolAtom) {
							XWmProtocols.this.wmProtocolsAtomId = wmProtocolAtom.intValue();
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to intern WM_PROTOCOLS atom.",
											t);
						}
					});
	}

	public ListenableFuture<Boolean> hasWmProtocols(final DisplaySurface xWindow,
													final int... atomId) {

		final ListenableFuture<Set<Integer>> wmProtocolsFuture = this.wmExecutor.submit(new Callable<Set<Integer>>() {
			@Override
			public Set<Integer> call() {
				Set<Integer> protocols = XWmProtocols.this.protocolsByXWindowCache.get(xWindow
						.getDisplaySurfaceHandle().getNativeHandle());
				if (protocols == null) {
					protocols = new HashSet<Integer>();
					trackWmProtocols(	protocols,
										xWindow);
				}
				return protocols;
			}
		});

		return transform(	wmProtocolsFuture,
							new Function<Set<Integer>, Boolean>() {
								@Override
								public Boolean apply(final Set<Integer> input) {
									// TODO Auto-generated method stub
									return null;
								}
							});

	}

	private void trackWmProtocols(	final Set<Integer> protocols,
									final DisplaySurface xWindow) {
		xWindow.register(new XPropertyChanged() {
			@Override
			public void onXPropertyChanged(final xcb_property_notify_event_t property_notify_event) {
				final int atom = property_notify_event.getAtom();

				// TODO check if atom was deleted
				// final int atomState = property_notify_event.getState();
				// if(atomState == ...){
				// return;
				// }

				if (atom == XWmProtocols.this.wmProtocolsAtomId) {
					refreshWmProtocolsCache(protocols,
											xWindow);
				}
			}
		});
		refreshWmProtocolsCache(protocols,
								xWindow);
	}

	private void refreshWmProtocolsCache(	final Set<Integer> protocols,
											final DisplaySurface xWindow) {
		final int window = ((Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle()).intValue();
		final xcb_get_property_cookie_t get_property_cookie = xcb_icccm_get_wm_protocols(	this.xConnection.getConnectionRef(),
																							window,
																							this.wmProtocolsAtomId);
		this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {

				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_icccm_get_wm_protocols_reply_t wm_protocols = new xcb_icccm_get_wm_protocols_reply_t();
				final short stat = xcb_icccm_get_wm_protocols_reply(XWmProtocols.this.xConnection.getConnectionRef(),
																	get_property_cookie,
																	wm_protocols,
																	e);
				if ((stat == 0) || (xcb_generic_error_t.getCPtr(e) != 0)) {
					logger.error(	"Failed to get wm_protocols property from window={}",
									window);
					return null;
				}

				final IntBuffer protocolsReply = wm_protocols.getAtoms().order(ByteOrder.nativeOrder()).asIntBuffer();
				final int replyLength = wm_protocols.getAtoms_len();

				protocols.clear();
				while (replyLength > 0) {
					final int protocolAtomId = protocolsReply.get();
					protocols.add(Integer.valueOf(protocolAtomId));
				}

				notifyProtocolListeners(xWindow);

				return null;
			}
		});
	}
}