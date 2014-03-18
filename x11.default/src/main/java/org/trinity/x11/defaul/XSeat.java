package org.trinity.x11.defaul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_set_input_focus;
import static org.freedesktop.xcb.xcb_input_focus_t.XCB_INPUT_FOCUS_NONE;

/**
 *
 */
public class XSeat {

	private static final Logger LOG = LoggerFactory.getLogger(XSeat.class);

	private final XTime         xTime;
	private final XEventChannel xEventChannel;

	@Inject
	XSeat(final XTime xTime,
		  final XEventChannel xEventChannel) {
		this.xTime = xTime;
		this.xEventChannel = xEventChannel;
	}

	public void giveInputFocus(final XWindow xWindow) {
		final Integer winId = xWindow.getNativeHandle();
		final int time = this.xTime.getTime();
		LOG.debug("[winId={}] set input focus.",
				  winId);
		xcb_set_input_focus(this.xEventChannel.getXcbConnection(),
							(short) XCB_INPUT_FOCUS_NONE,
							winId,
							time);
		xcb_flush(this.xEventChannel.getXcbConnection());
	}
}
