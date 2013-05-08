package org.trinity.shellplugin.wm.x11.impl.scene;

import java.util.concurrent.Callable;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.ReceivesPointerInput;
import org.trinity.shellplugin.wm.x11.impl.XConnection;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;
import org.trinity.shellplugin.wm.x11.impl.protocol.XClientMessageSender;
import org.trinity.shellplugin.wm.x11.impl.protocol.XPropertyChanged;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;

import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name;
import static org.freedesktop.xcb.LibXcb.xcb_icccm_get_wm_name_reply;

import static com.google.common.util.concurrent.Futures.addCallback;

public class ClientBarItem implements HasText, ReceivesPointerInput {

	private static Logger logger = LoggerFactory.getLogger(ClientBarItem.class);

	private final ShellSurface client;
	private final XAtomCache xAtomCache;
	private final ListeningExecutorService wmExecutor;
	private final XClientMessageSender xClientMessageSender;
	private final XConnection xConnection;
	private String clientName = "";

	private final ReceivesPointerInput closeButton = new ReceivesPointerInput() {
		// called by shell thread.
		@Override
		public void onPointerInput(final PointerInput pointerInput) {
			if (pointerInput.getMomentum() == Momentum.STOPPED) {
				handleClientCloseRequest();
			}
		}
	};

	@AssistedInject
	ClientBarItem(	@Named("WindowManager") final ListeningExecutorService wmExecutor,
					final XClientMessageSender xClientMessageSender,
					final XAtomCache xAtomCache,
					final XConnection xConnection,
					@Assisted final ShellSurface client) {
		this.xClientMessageSender = xClientMessageSender;
		this.client = client;
		this.xAtomCache = xAtomCache;
		this.xConnection = xConnection;
		this.wmExecutor = wmExecutor;
		addCallback(client.getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface clientXWindow) {
							clientXWindow.register(new XPropertyChanged() {
								// called by window manager thread
								@Override
								public void onXPropertyChanged(final xcb_property_notify_event_t property_notify_event) {
									updateClientName(clientXWindow);
								}
							});
							updateClientName(clientXWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Error getting display surface from client.",
											t);
						}
					},
					wmExecutor);
	}

	// called by window manager thread
	public void updateClientName(final DisplaySurface clientXWindow) {
		final int window = (Integer) clientXWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_property_cookie_t = xcb_icccm_get_wm_name(	this.xConnection.getConnectionRef(),
																						window);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_icccm_get_text_property_reply_t prop = new xcb_icccm_get_text_property_reply_t();

		this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {

				final short stat = xcb_icccm_get_wm_name_reply(	ClientBarItem.this.xConnection.getConnectionRef(),
																get_property_cookie_t,
																prop,
																e);
				if (stat == 0) {
					logger.error(	"Error retrieving wm_name reply from client={}",
									window);
					return null;
				}

				// TODO better text parsing.
				setText(prop.getName());

				return null;
			}
		});
	}

	public ReceivesPointerInput getCloseButton() {
		return this.closeButton;
	}

	// called by shell thread.
	private void handleClientCloseRequest() {
		addCallback(this.client.getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface clientXWindow) {
							sendWmDeleteMessage(clientXWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Error getting display surface from client.",
											t);
						}
					},
					this.wmExecutor);
	}

	private void sendWmDeleteMessage(final DisplaySurface clientXWindow) {
		// this.xClientMessageSender.sendMessage( clientXWindow,
		// type,
		// Format.Integer,
		// message);
	}

	@PropertyChanged("text")
	public void setText(final String text) {
		this.clientName = text;
	}

	@Override
	public String getText() {

		return this.clientName;
	}

	// called by shell thread.
	@Override
	public void onPointerInput(final PointerInput pointerInput) {
		addCallback(this.client.getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface clientXWindow) {
							clientXWindow.setInputFocus();
							ClientBarItem.this.client.doRaise();
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Error getting display surface from client.",
											t);
						}
					},
					this.wmExecutor);

		// do something with client, eg raise & give focus?
	}
}