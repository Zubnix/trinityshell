package org.trinity.wayland.input.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import org.trinity.wayland.input.Seat;

public class GLWindowSeat implements MouseListener, KeyListener {

    private final Seat seat;

    GLWindowSeat(Seat seat) {
        this.seat = seat;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        final long time = e.getWhen();
        final int absX = e.getX();
        final int absY = e.getY();

        this.seat.handlePointerMove(time,
                                    absX,
                                    absY);
    }


    @Override
    public void mouseDragged(final MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(final MouseEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {

    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }
}
