package org.trinity.foundation.display.x11.impl;

import org.freedesktop.xcb.xcb_screen_t;
import org.trinity.foundation.api.shared.Size;
import org.trinity.foundation.display.x11.api.XScreen;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Created with IntelliJ IDEA. User: erik Date: 7/12/13 Time: 11:28 PM To change
 * this template use File | Settings | File Templates.
 */
@Immutable
public class XScreenImpl implements XScreen {

	private final xcb_screen_t screen;

	public XScreenImpl(@Nonnull final xcb_screen_t screen) {
		this.screen = screen;
	}

	@Override
	public xcb_screen_t getScreenReference() {
		return screen;
	}

	@Override
	public Size getSize() {
		return new Size(this.screen.getWidth_in_pixels(),
						this.screen.getHeight_in_pixels());
	}
}
