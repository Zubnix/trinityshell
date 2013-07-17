package org.trinity.foundation.display.x11.impl;

import org.freedesktop.xcb.xcb_screen_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.foundation.display.x11.api.XScreen;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Created with IntelliJ IDEA. User: erik Date: 7/12/13 Time: 11:28 PM To change
 * this template use File | Settings | File Templates.
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class XScreenImpl implements XScreen {

	private final xcb_screen_t xcb_screen;

	public XScreenImpl(@Nonnull xcb_screen_t xcb_screen) {
		this.xcb_screen = xcb_screen;
	}

	@Override
	public xcb_screen_t getScreenReference() {
		return xcb_screen;
	}

	@Override
	public Size getSize() {
		return new Size(this.xcb_screen.getWidth_in_pixels(),
						this.xcb_screen.getHeight_in_pixels());
	}
}
