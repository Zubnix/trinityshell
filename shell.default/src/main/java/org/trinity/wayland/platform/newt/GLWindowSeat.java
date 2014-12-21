package org.trinity.wayland.platform.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.trinity.wayland.output.JobExecutor;
import org.trinity.wayland.output.Seat;
import org.trinity.wayland.output.events.Button;
import org.trinity.wayland.output.events.Motion;

public class GLWindowSeat implements MouseListener, KeyListener {

    private final Seat        seat;
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

        this.jobExecutor.submit(() -> this.seat.getPointer()
                                               .post(new Button((int) time,
                                                                button,
                                                                WlPointerButtonState.PRESSED)));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final long time = e.getWhen();
        final short button = e.getButton();

        this.jobExecutor.submit(() -> this.seat.getPointer()
                                               .post(new Button((int) time,
                                                                button,
                                                                WlPointerButtonState.RELEASED)));
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        final long time = e.getWhen();
        final int x = e.getX();
        final int y = e.getY();

        this.jobExecutor.submit(() -> this.seat.getPointer()
                                               .post(new Motion((int) time,
                                                                x,
                                                                y)));
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
