package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;

@AutoFactory(className = "KeyboardFactory")
public class Keyboard {

    private final EventBus inputBus = new EventBus();

    Keyboard() {
    }

    public void register(final Object listener) {
        this.inputBus.register(listener);
    }

    public void unregister(final Object listener) {
        this.inputBus.unregister(listener);
    }
}
