package org.trinity.shellplugin.wm.x11.impl.scene;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_send_event;
import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;
import static org.freedesktop.xcb.LibXcbConstants.XCB_CURRENT_TIME;

import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;

import org.freedesktop.xcb.IntArray;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_icccm_get_text_property_reply_t;
import org.freedesktop.xcb.xcb_icccm_get_wm_protocols_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shellplugin.wm.api.HasText;
import org.trinity.shellplugin.wm.api.ReceivesPointerInput;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;
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

@ExecutionContext(ShellExecutor.class)
public class ClientBarElement implements HasText, ReceivesPointerInput {

	private static Logger LOG = LoggerFactory.getLogger(ClientBarElement.class);
	private final ListeningExecutorService shellExecutor;
	private final WmName wmName;
	private final WmProtocols wmProtocols;
	private final ReceivesPointerInput closeButton = new ReceivesPointerInput() {
		// called by shell thread.
		@Override
		public void onPointerInput(final PointerInput pointerInput) {
			if (pointerInput.getMomentum() == Momentum.STOPPED) {
				handleClientCloseRequest();
			}
		}
	};
	private final XConnection xConnection;
	private DisplaySurface clientXWindow;
	private String clientName = "";
	private boolean canSendWmDeleteMsg;
	private int wmDeleteWindowAtomId;
	private int wmProtocolsAtomId;

	@AssistedInject
	ClientBarElement(	@ShellExecutor final ListeningExecutorService shellExecutor,
						final XConnection xConnection,
						final WmName wmName,
						final WmProtocols wmProtocols,
						final XAtomCache xAtomCache,
						@Assisted final DisplaySurface clientXWindow) {
		this.xConnection = xConnection;
		this.wmName = wmName;
		this.wmProtocols = wmProtocols;
		this.shellExecutor = shellExecutor;

		this.wmDeleteWindowAtomId = xAtomCache.getAtom("WM_DELETE_WINDOW");
		this.wmProtocolsAtomId = xAtomCache.getAtom("WM_PROTOCOLS");
		setClientXWindow(clientXWindow);
	}

	//called by shell executor
	private void setClientXWindow(final DisplaySurface clientXWindow) {
		ClientBarElement.this.clientXWindow = clientXWindow;

		//FIXME execution context for protocol notifications is wrong

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
							LOG.error(	"Failed to get wm_protocols protocol",
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

	// called by shell executor
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
							LOG.error(	"Failed to get wm name protocol",
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
		// FIXME use xdisplayexecutor
		this.shellExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				if (ClientBarElement.this.canSendWmDeleteMsg) {
					sendWmDeleteMessage(ClientBarElement.this.clientXWindow);
				} else {
					ClientBarElement.this.clientXWindow.destroy();
				}
				return null;
			}
		});
	}

	private void sendWmDeleteMessage(final DisplaySurface clientXWindow) {
		final Integer winId = (Integer) clientXWindow.getDisplaySurfaceHandle().getNativeHandle();

		final xcb_client_message_event_t client_message_event = new xcb_client_message_event_t();
		client_message_event.setFormat((short) 32);
		final IntArray data32 = IntArray.frompointer(client_message_event.getData().getData32());
		data32.setitem(	0,
						this.wmDeleteWindowAtomId);
		data32.setitem(	1,
						XCB_CURRENT_TIME);
		client_message_event.setType(this.wmProtocolsAtomId);
		client_message_event.setWindow(winId.intValue());
		client_message_event.setResponse_type((short) XCB_CLIENT_MESSAGE);

		final xcb_generic_event_t generic_event = new xcb_generic_event_t(	xcb_client_message_event_t.getCPtr(client_message_event),
																			false);
		xcb_send_event(	this.xConnection.getConnectionReference(),
						(short) 0,
						winId.intValue(),
						xcb_event_mask_t.XCB_EVENT_MASK_NO_EVENT,
						generic_event);
		xcb_flush(this.xConnection.getConnectionReference());
	}

	@Override
	public String getText() {

		return this.clientName;
	}

	@PropertyChanged("text")
	public void setText(final String text) {
		this.clientName = text;
	}

	// called by shell thread.
	@Override
	public void onPointerInput(final PointerInput pointerInput) {
		// do something with client, eg raise & give focus
		// FIXME dont use executor, already invoked by shell thread.
		this.shellExecutor.submit(new Runnable() {
			@Override
			public void run() {
				ClientBarElement.this.clientXWindow.setInputFocus();
				ClientBarElement.this.clientXWindow.raise();
			}
		});
	}
}
