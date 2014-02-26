package org.trinity.foundation.display.x11.impl;

import com.google.auto.value.AutoValue;
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
@AutoValue
public abstract class XScreenImpl implements XScreen {

    public static XScreen create(@Nonnull final xcb_screen_t screen){
        return new AutoValue_XScreenImpl(screen);
    }

	@Override
	public Size getSize() {
		return Size.create(getScreenReference().getWidth_in_pixels(),
                getScreenReference().getHeight_in_pixels());
	}
}
