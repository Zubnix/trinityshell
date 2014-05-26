package org.trinity.wayland.defaul.protocol;

import org.freedesktop.wayland.protocol.wl_touch;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlTouch implements wl_touch.Requests3 {

    @Inject
    WlTouch() {
    }

    @Override
    public void release(final wl_touch.Resource resource) {

    }
}
