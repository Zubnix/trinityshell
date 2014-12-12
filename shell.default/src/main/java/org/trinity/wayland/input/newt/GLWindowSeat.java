package org.trinity.wayland.input.newt;

import com.google.auto.factory.Provided;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.trinity.wayland.input.Seat;
import org.trinity.wayland.output.JobExecutor;

public class GLWindowSeat implements MouseListener, KeyListener {

    private final Seat seat;
    private final JobExecutor jobExecutor;

    GLWindowSeat(final Seat seat,
                 final JobExecutor jobExecutor) {
        this.seat = seat;
        this.jobExecutor = jobExecutor;
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

        final long time = e.getWhen();
        final short button = e.getButton();

        this.jobExecutor.submit(() -> this.seat.handleButton((int) time,
                                                             button,
                                                             WlPointerButtonState.PRESSED));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final long time = e.getWhen();
        final short button = e.getButton();

        this.jobExecutor.submit(() -> this.seat.handleButton((int) time,
                                                             button,
                                                             WlPointerButtonState.RELEASED));
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        final long time = e.getWhen();
        final int absX = e.getX();
        final int absY = e.getY();

        this.jobExecutor.submit(() -> this.seat.handlePointerMove((int) time,
                                                                  absX,
                                                                  absY));
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
