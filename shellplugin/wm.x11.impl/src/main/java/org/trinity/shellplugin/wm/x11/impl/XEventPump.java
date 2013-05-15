package org.trinity.shellplugin.wm.x11.impl;

import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class XEventPump implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(XEventPump.class);

	private final ListeningExecutorService xEventPumpExecutor = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(final Runnable arg0) {
			return new Thread(	arg0,
								"wm-x-eventpump-executor");
		}
	}));

	private final ListeningExecutorService wmExecutor;
	private final XEventHandler xEventHandler;
	private final XConnection xConnection;

	@Inject
	XEventPump(	final XConnection xConnection,
				@Named("WindowManager") final ListeningExecutorService wmExecutor,
				final XEventHandler xEventHandler) {
		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;
		this.xEventHandler = xEventHandler;

		this.xEventPumpExecutor.submit(this);
	}

	@Override
	public void run() {
		final xcb_generic_event_t event = xcb_wait_for_event(XEventPump.this.xConnection.getConnectionRef());
		if (xcb_connection_has_error(this.xConnection.getConnectionRef()) != 0) {
			final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
			XEventPump.logger.error(errorMsg);
			throw new Error(errorMsg);
		}

		// pass x event from x-event-pump thread to wm-executor thread.
		this.wmExecutor.submit(new Runnable() {
			@Override
			public void run() {
				XEventPump.this.xEventHandler.handleXEvent(event);
				event.delete();
			}
		});

		// schedule next event retrieval
		this.xEventPumpExecutor.submit(this);
	}
}