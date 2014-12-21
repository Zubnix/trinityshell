package org.trinity.wayland.output.events;

import org.freedesktop.wayland.shared.WlPointerButtonState;

public class Button {
    private final int time;
    private final int button;
    private final WlPointerButtonState buttonState;

    public Button(final int time,
                  final int button,
                  final WlPointerButtonState buttonState) {
        this.time = time;
        this.button = button;
        this.buttonState = buttonState;
    }

    public int getButton() {
        return this.button;
    }

    public WlPointerButtonState getButtonState() {
        return this.buttonState;
    }

    public int getTime() {
        return this.time;
    }
}
