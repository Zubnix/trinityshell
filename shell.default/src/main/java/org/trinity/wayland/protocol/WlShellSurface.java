package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.*;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.output.Pointer;
import org.trinity.wayland.output.events.Motion;
import org.trinity.wayland.protocol.events.ResourceDestroyed;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Point;
import java.util.Optional;
import java.util.Set;

@AutoFactory(className = "WlShellSurfaceFactory")
public class WlShellSurface extends EventBus implements WlShellSurfaceRequests, ProtocolObject<WlShellSurfaceResource> {

    private final Set<WlShellSurfaceResource> resources = Sets.newHashSet();

    private final WlSurface wlSurface;

    WlShellSurface(final WlSurface wlSurface) {
        this.wlSurface = wlSurface;
    }

    @Override
    public void pong(final WlShellSurfaceResource requester,
                     final int serial) {

    }

    @Override
    public void move(final WlShellSurfaceResource requester,
                     @Nonnull final WlSeatResource seat,
                     final int serial) {
        final WlSeat wlSeat = (WlSeat) seat.getImplementation();
        final Optional<WlPointer> optionalWlPointer = wlSeat.getOptionalWlPointer();
        if (!optionalWlPointer.isPresent()) {
            //there's no pointer on the system
            return;
        }
        final WlPointer wlPointer = optionalWlPointer.get();
        if (!wlPointer.getGrabSerial()
                      .isPresent()) {
            //there's no pointer grab active on the system
            return;
        }
        if (!wlPointer.getGrabSerial()
                      .get()
                      .equals(serial)) {
            //surface asking for move does not have the pointer grab
            return;
        }
        //listen for pointer motion
        final Pointer pointer = wlSeat.getSeat()
                                      .getPointer();
        //keep reference to pointer 'start' position
        final Point startPosition = pointer.getPosition();

        final Object motionListener = new Object() {
            @Subscribe
            public void handle(final Motion motion) {
                if (wlPointer.getGrabSerial()
                             .isPresent()
                        && wlPointer.getGrabSerial()
                                    .get()
                                    .equals(serial)) {
                    //move surface
                    move(startPosition,
                         motion);
                }
                else {
                    //another surface has the grab, stop listening for pointer motion.
                    pointer.unregister(this);
                    //no need to move the surface anymore, so no need listen for resource destroy events
                    unregister(this);
                }
            }

            @Subscribe
            public void handle(final ResourceDestroyed resourceDestroyed) {
                //don't listen for pointer events if the resource is destroyed
                pointer.unregister(this);
                unregister(this);
            }
        };
        pointer.register(motionListener);
        register(motionListener);
    }


    private void move(final Point startPosition,
                      final Motion motion) {
        final int deltaX = motion.getX() - startPosition.getX();
        final int deltaY = motion.getY() - startPosition.getY();

        final ShellSurface shellSurface = this.wlSurface.getShellSurface();

        //FIXME we need a better way to manipulate a surface's position
        shellSurface.accept(config -> {
            config.attachBuffer(shellSurface.getBuffer(),
                                deltaX,
                                deltaY);
            config.commit();
        });
    }

    @Override
    public void resize(final WlShellSurfaceResource requester,
                       @Nonnull final WlSeatResource seat,
                       final int serial,
                       final int edges) {

    }

    @Override
    public void setToplevel(final WlShellSurfaceResource requester) {

    }

    @Override
    public void setTransient(final WlShellSurfaceResource requester,
                             @Nonnull final WlSurfaceResource parent,
                             final int x,
                             final int y,
                             final int flags) {

    }

    @Override
    public void setFullscreen(final WlShellSurfaceResource requester,
                              final int method,
                              final int framerate,
                              final WlOutputResource output) {

    }

    @Override
    public void setPopup(final WlShellSurfaceResource requester,
                         @Nonnull final WlSeatResource seat,
                         final int serial,
                         @Nonnull final WlSurfaceResource parent,
                         final int x,
                         final int y,
                         final int flags) {

    }

    @Override
    public void setMaximized(final WlShellSurfaceResource requester,
                             final WlOutputResource output) {

    }

    @Override
    public void setTitle(final WlShellSurfaceResource requester,
                         @Nonnull final String title) {

    }

    @Override
    public void setClass(final WlShellSurfaceResource requester,
                         @Nonnull final String class_) {

    }

    @Override
    public Set<WlShellSurfaceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlShellSurfaceResource create(final Client client,
                                         final int version,
                                         final int id) {
        return new WlShellSurfaceResource(client,
                                          version,
                                          id,
                                          this);
    }
}
