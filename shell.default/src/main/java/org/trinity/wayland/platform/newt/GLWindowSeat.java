package org.trinity.wayland.platform.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.trinity.wayland.output.JobExecutor;
import org.trinity.wayland.protocol.WlSeat;

public class GLWindowSeat implements MouseListener, KeyListener {

    private final WlSeat      wlSeat;
    private final JobExecutor jobExecutor;

    GLWindowSeat(final WlSeat wlSeat,
                 final JobExecutor jobExecutor) {
        this.wlSeat = wlSeat;
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

        this.wlSeat.getOptionalWlPointer()
                   .ifPresent(wlPointer -> this.jobExecutor.submit(() -> wlPointer.getPointer()
                                                                                  .button(wlPointer,
                                                                                          (int) time,
                                                                                          button,
                                                                                          WlPointerButtonState.PRESSED)));
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final long time = e.getWhen();
        final short button = e.getButton();

        this.wlSeat.getOptionalWlPointer()
                   .ifPresent(wlPointer -> this.jobExecutor.submit(() -> wlPointer.getPointer()
                                                                                  .button(wlPointer,
                                                                                          (int) time,
                                                                                          button,
                                                                                          WlPointerButtonState.RELEASED)));
    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        final long time = e.getWhen();
        final int x = e.getX();
        final int y = e.getY();

        this.wlSeat.getOptionalWlPointer()
                   .ifPresent(wlPointer -> this.jobExecutor.submit(() -> wlPointer.getPointer()
                                                                                  .motion(wlPointer,
                                                                                          (int) time,
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
