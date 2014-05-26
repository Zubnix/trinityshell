package org.trinity.wayland.defaul.protocol;

import org.freedesktop.wayland.protocol.wl_keyboard;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlKeyboard implements wl_keyboard.Requests3 {

    @Inject
    WlKeyboard() {
    }

    @Override
    public void release(final wl_keyboard.Resource resource) {

    }
}
