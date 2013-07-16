package org.trinity.foundation.display.x11.api;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_screen_t;
import org.trinity.foundation.api.display.Screen;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

/**
 * Created with IntelliJ IDEA. User: erik Date: 7/12/13 Time: 11:10 PM To change
 * this template use File | Settings | File Templates.
 */
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public interface XScreen extends Screen {
	xcb_screen_t getScreenReference();
}
