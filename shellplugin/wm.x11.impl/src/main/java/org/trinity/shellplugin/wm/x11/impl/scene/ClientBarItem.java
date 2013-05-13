package org.trinity.shellplugin.wm.x11.impl.scene;

import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;

import org.freedesktop.xcb.xcb_client_message_data_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.freedesktop.xcb.xcb_icccm_get_wm_protocols_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.ReceivesPointerInput;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;
import org.trinity.shellplugin.wm.x11.impl.protocol.XClientMessageSender;
import org.trinity.shellplugin.wm.x11.impl.protocol.icccm.ProtocolListener;
import org.trinity.shellplugin.wm.x11.impl.protocol.icccm.WmName;
import org.trinity.shellplugin.wm.x11.impl.protocol.icccm.WmProtocols;

import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Named;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CURRENT_TIME;

import static com.google.common.util.concurrent.Futures.addCallback;

public class ClientBarItem implements HasText, ReceivesPointerInput {

	private static Logger logger = LoggerFactory.getLogger(ClientBarItem.class);

	private final ListeningExecutorService wmExecutor;

	private final WmName wmName;
	private final WmProtocols wmProtocols;
	private final XClientMessageSender xClientMessageSender;

	private DisplaySurface clientXWindow;
	private String clientName = "";
	private boolean canSendWmDeleteMsg;

	private int wmDeleteWindowAtomId;
	private int wmProtocolsAtomId;

	private final ReceivesPointerInput closeButton = new ReceivesPointerInput() {
		// called by shell thread.
		@Override
		public void onPointerInput(final PointerInput pointerInput) {
			if (pointerInput.getMomentum() == Momentum.STOPPED) {
				handleClientCloseRequest();
			}
		}
	};

	private final XAtomCache xAtomCache;

	@AssistedInject
	ClientBarItem(	@Named("WindowManager") final ListeningExecutorService wmExecutor,
					final XClientMessageSender xClientMessageSender,
					final WmName wmName,
					final WmProtocols wmProtocols,
					final XAtomCache xAtomCache,
					@Assisted final ShellSurface client) {
		this.xClientMessageSender = xClientMessageSender;
		this.wmName = wmName;
		this.wmProtocols = wmProtocols;
		this.wmExecutor = wmExecutor;
		this.xAtomCache = xAtomCache;

		addCallback(client.getDisplaySurface(),
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface clientXWindow) {
							ClientBarItem.this.wmDeleteWindowAtomId = xAtomCache.getAtom("WM_DELETE_WINDOW");
							ClientBarItem.this.wmProtocolsAtomId = xAtomCache.getAtom("WM_PROTOCOLS");
							setClientXWindow(clientXWindow);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Error getting display surface from client.",
											t);
						}
					},
					wmExecutor);
	}

	private void setClientXWindow(final DisplaySurface clientXWindow) {
		ClientBarItem.this.clientXWindow = clientXWindow;

		// client name handling
		this.wmName.addProtocolListener(clientXWindow,
										new ProtocolListener<xcb_icccm_get_text_property_reply_t>() {
											@Override
											@Subscribe
											public void onProtocolChanged(final Optional<xcb_icccm_get_text_property_reply_t> protocol) {
												updateClientName(protocol);
											}
										});
		queryClientName(clientXWindow);

		// client close request handling
		this.wmProtocols.addProtocolListener(	clientXWindow,
												new ProtocolListener<xcb_icccm_get_wm_protocols_reply_t>() {
													@Override
													@Subscribe
													public void onProtocolChanged(final Optional<xcb_icccm_get_wm_protocols_reply_t> protocol) {
														updateCanSendWmDeleteMsg(protocol);
													}
												});
		queryCanSendWmDeleteMsg(clientXWindow);
	}

	private void queryCanSendWmDeleteMsg(final DisplaySurface clientXWindow) {
		final ListenableFuture<Optional<xcb_icccm_get_wm_protocols_reply_t>> wmProtocolFuture = this.wmProtocols
				.get(clientXWindow);
		addCallback(wmProtocolFuture,
					new FutureCallback<Optional<xcb_icccm_get_wm_protocols_reply_t>>() {
						@Override
						public void onSuccess(final Optional<xcb_icccm_get_wm_protocols_reply_t> result) {
							updateCanSendWmDeleteMsg(result);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to get wm_protocols protocol",
											t);
						}
					});
	}

	private void updateCanSendWmDeleteMsg(final Optional<xcb_icccm_get_wm_protocols_reply_t> optionalWmProtocolReply) {
		if (optionalWmProtocolReply.isPresent()) {
			final xcb_icccm_get_wm_protocols_reply_t wm_protocols_reply = optionalWmProtocolReply.get();
			final IntBuffer wmProtocolsBuffer = wm_protocols_reply.getAtoms().order(ByteOrder.nativeOrder())
					.asIntBuffer();
			int nroWmProtocols = wm_protocols_reply.getAtoms_len();
			while (nroWmProtocols > 0) {
				this.canSendWmDeleteMsg = wmProtocolsBuffer.get() == this.wmDeleteWindowAtomId;
				if (this.canSendWmDeleteMsg) {
					break;
				}
				nroWmProtocols--;
			}
		} else {
			this.canSendWmDeleteMsg = false;
		}
	}

	// called by window manager thread
	public void queryClientName(final DisplaySurface clientXWindow) {
		final ListenableFuture<Optional<xcb_icccm_get_text_property_reply_t>> wmNameFuture = this.wmName
				.get(clientXWindow);
		addCallback(wmNameFuture,
					new FutureCallback<Optional<xcb_icccm_get_text_property_reply_t>>() {
						@Override
						public void onSuccess(final Optional<xcb_icccm_get_text_property_reply_t> optionalTextProperty) {
							updateClientName(optionalTextProperty);
						}

						@Override
						public void onFailure(final Throwable t) {
							logger.error(	"Failed to get wm name protocol",
											t);
						}
					});
	}

	private void updateClientName(final Optional<xcb_icccm_get_text_property_reply_t> optionalTextProperty) {
		if (optionalTextProperty.isPresent()) {
			setText(optionalTextProperty.get().getName());
		} else {
			setText("");
		}
	}

	public ReceivesPointerInput getCloseButton() {
		return this.closeButton;
	}

	// called by shell thread.
	private void handleClientCloseRequest() {
		this.wmExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				if (ClientBarItem.this.canSendWmDeleteMsg) {
					sendWmDeleteMessage(ClientBarItem.this.clientXWindow);
				} else {
					ClientBarItem.this.clientXWindow.destroy();
				}
				return null;
			}
		});
	}

	private void sendWmDeleteMessage(final DisplaySurface clientXWindow) {

		final xcb_client_message_data_t client_message_data = new xcb_client_message_data_t();
		client_message_data.setData32(new int[] { this.wmDeleteWindowAtomId, XCB_CURRENT_TIME, 0, 0, 0 });
		this.xClientMessageSender.sendMessage(	clientXWindow,
												this.wmProtocolsAtomId,
												32,
												client_message_data);
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
		// do something with client, eg raise & give focus
		this.wmExecutor.submit(new Runnable() {
			@Override
			public void run() {
				ClientBarItem.this.clientXWindow.setInputFocus();
				ClientBarItem.this.clientXWindow.raise();
			}
		});
	}
}