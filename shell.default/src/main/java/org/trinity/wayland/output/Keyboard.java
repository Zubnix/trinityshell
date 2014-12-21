package org.trinity.wayland.output;

import com.google.common.eventbus.EventBus;

import javax.inject.Inject;

public class Keyboard {

    private final EventBus inputBus = new EventBus();

    @Inject
    Keyboard() {
    }

    public void register(final Object listener) {
        this.inputBus.register(listener);
    }

    public void unregister(final Object listener) {
        this.inputBus.unregister(listener);
    }
}
