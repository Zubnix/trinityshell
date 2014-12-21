package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

@AutoFactory(className = "SeatFactory")
public class Seat {
    private final Pointer  pointer;
    private final Keyboard keyboard;

    public Seat(@Provided final Pointer pointer,
                @Provided final Keyboard keyboard) {
        this.pointer = pointer;
        this.keyboard = keyboard;
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    public Keyboard getKeyboard() {
        return this.keyboard;
    }
}
