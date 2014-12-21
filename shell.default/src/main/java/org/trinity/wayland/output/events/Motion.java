package org.trinity.wayland.output.events;

public class Motion {
    private final int time;
    private final int x;
    private final int y;

    public Motion(final int time,
                  final int x,
                  final int y) {
        this.time = time;
        this.x = x;
        this.y = y;
    }

    public int getTime() {
        return this.time;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
