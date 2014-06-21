package org.trinity.x11;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.annotation.Nonnull;

public interface XWindowRenderer {

    public static EventBus DISPATCHER(final XWindow xWindow) {
        return new EventBus() {{
            register(new Object() {
                @Subscribe
                public void handle(@Nonnull final XWindowRenderer xWindowRenderer) {
                    xWindowRenderer.visit(xWindow);
                }
            });
        }};
    }

    void visit(XWindow buffer);
}
