package org.trinity.wayland.defaul;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
public class EagerSingletons {
    @Inject
    static WlDataDeviceManager wlDataDeviceManager;

    @Inject
    EagerSingletons() {
    }
}
