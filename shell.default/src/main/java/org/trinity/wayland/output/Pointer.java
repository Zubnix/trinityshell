package org.trinity.wayland.output;

import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.trinity.wayland.output.events.Button;
import org.trinity.wayland.output.events.Motion;

import javax.inject.Inject;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import java.util.HashSet;
import java.util.Set;

public class Pointer {
    private final EventBus     inputBus       = new EventBus();
    private final Set<Integer> pressedButtons = new HashSet<>();

    private PointImmutable position = new Point();

    @Inject
    Pointer() {
    }

    public PointImmutable getPosition() {
        return this.position;
    }

    public void post(final Motion motion) {
        this.position = new Point(motion.getX(),
                                  motion.getY());
        this.inputBus.post(motion);
    }

    public void post(final Button button) {
        final int buttonCode = button.getButton();
        if (button.getButtonState() == WlPointerButtonState.PRESSED) {
            this.pressedButtons.add(buttonCode);
        }
        else {
            this.pressedButtons.remove(buttonCode);
        }
        this.inputBus.post(button);
    }

    public boolean isButtonPressed(final int button) {
        return this.pressedButtons.contains(button);
    }

    public void register(final Object listener) {
        this.inputBus.register(listener);
    }

    public void unregister(final Object listener) {
        this.inputBus.unregister(listener);
    }
}
