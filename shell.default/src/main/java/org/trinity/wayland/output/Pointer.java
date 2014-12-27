package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.WlPointerResource;
import org.freedesktop.wayland.server.WlSurfaceResource;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.freedesktop.wayland.util.Fixed;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.output.events.Motion;
import org.trinity.wayland.protocol.WlPointer;
import org.trinity.wayland.protocol.WlSurface;
import org.trinity.wayland.protocol.events.ResourceDestroyed;

import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AutoFactory
public class Pointer {
    private final EventBus     inputBus       = new EventBus();
    private final Set<Integer> pressedButtons = new HashSet<>();

    private PointImmutable position = new Point();

    private Optional<WlSurface> grab       = Optional.empty();
    private Optional<Integer>   grabSerial = Optional.empty();
    private Optional<WlSurface> focus      = Optional.empty();

    private int buttonsPressed;

    private final Compositor compositor;
    private final Display    display;

    Pointer(@Provided final Display display,
            final Compositor compositor) {
        this.display = display;
        this.compositor = compositor;
    }

    public PointImmutable getPosition() {
        return this.position;
    }

    public void motion(final WlPointer wlPointer,
                       final int time,
                       final int x,
                       final int y) {
        doMotion(wlPointer,
                 time,
                 x,
                 y);
        this.inputBus.post(new Motion(time,
                                      x,
                                      y));
    }

    public void button(final WlPointer wlPointer,
                       final int time,
                       final int button,
                       final WlPointerButtonState buttonState) {
        if (buttonState == WlPointerButtonState.PRESSED) {
            this.pressedButtons.add(button);
        }
        else {
            this.pressedButtons.remove(button);
        }
        doButton(wlPointer,
                 time,
                 button,
                 buttonState);
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

    public void move(final WlSurfaceResource wlSurfaceResource,
                     final int grabSerial) {

        final WlSurface wlSurface = (WlSurface) wlSurfaceResource.getImplementation();
        //keep reference to surface position position, relative to the pointer position
        final PointImmutable pointerStartPosition = getPosition();
        final PointImmutable surfaceStartPosition = wlSurface.getShellSurface()
                                                             .getPosition();
        final PointImmutable pointerSurfaceDelta = new Point(pointerStartPosition.getX() - surfaceStartPosition.getX(),
                                                             pointerStartPosition.getY() - surfaceStartPosition.getY());

        final Object motionListener = new Object() {
            @Subscribe
            public void handle(final Motion motion) {
                if (getGrabSerial().isPresent()
                        && getGrabSerial().get()
                                          .equals(grabSerial)) {
                    //move surface
                    move(wlSurface,
                         pointerSurfaceDelta,
                         motion);
                }
                else {
                    //another surface has the grab, stop listening for pointer motion.
                    unregister(this);
                    //no need to move the surface anymore, so no need listen for resource destroy events
                    unregister(this);
                }
            }

            @Subscribe
            public void handle(final ResourceDestroyed resourceDestroyed) {
                //don't listen for pointer events if the resource is destroyed
                unregister(this);
                wlSurface.unregister(this);
            }
        };
        register(motionListener);
        wlSurface.register(motionListener);
    }

    private void move(final WlSurface wlSurface,
                      final PointImmutable pointerSurfaceDelta,
                      final Motion motion) {

        final ShellSurface shellSurface = wlSurface.getShellSurface();

        shellSurface.accept(config ->
                                    config.setPosition(new Point(motion.getX() - pointerSurfaceDelta.getX(),
                                                                 motion.getY() - pointerSurfaceDelta.getY())));
        this.compositor.requestRender(shellSurface);
    }

    private void doButton(final WlPointer wlPointer,
                          final int time,
                          final int button,
                          final WlPointerButtonState buttonState) {
        if (buttonState == WlPointerButtonState.PRESSED) {
            this.buttonsPressed++;
        }
        else if (this.buttonsPressed > 0) {
            //make sure we only decrement if we had a least one increment.
            //Is such a thing even possible? Yes it is. (Aliens pressing a button before starting compositor)
            this.buttonsPressed--;
        }

        if (this.grab.isPresent()) {
            //always report if we had a grabbed surface.
            reportButton(wlPointer,
                         this.grabSerial.get(),
                         time,
                         button,
                         buttonState);
        }

        if (this.buttonsPressed == 0) {
            //all buttons released for the grabbed surface
            this.grab = Optional.empty();
            this.grabSerial = Optional.empty();
        }
        else if (!this.grab.isPresent() && this.focus.isPresent()) {
            //no grab, but we do have a focus and a pressed button. Focused surface becomes grab.
            this.grab = this.focus;
            final int serial = this.display.nextSerial();
            this.grabSerial = Optional.of(serial);
            //report for the newly grabbed surface
            reportButton(wlPointer,
                         serial,
                         time,
                         button,
                         buttonState);
        }
    }

    private void reportButton(final WlPointer wlPointer,
                              final int serial,
                              final int time,
                              final int button,
                              final WlPointerButtonState buttonState) {
        final Optional<WlSurfaceResource> wlSurfaceResource = this.grab.get()
                                                                       .getResource();
        if (wlSurfaceResource.isPresent()) {
            final Optional<WlPointerResource> pointerResource = findPointerResource(wlPointer,
                                                                                    wlSurfaceResource.get());
            if (pointerResource.isPresent()) {
                pointerResource.get()
                               .button(serial,
                                       time,
                                       button,
                                       buttonState.getValue());
            }
        }
    }

    public void doMotion(final WlPointer wlPointer,
                         final int time,
                         final int x,
                         final int y) {
        this.position = new Point(x,
                                  y);

        final Optional<WlSurface> newFocus = this.compositor.getScene()
                                                            .findSurfaceAtCoordinate(x,
                                                                                     y);
        if (this.grab.isPresent() && !this.focus.equals(newFocus)) {
            //pointer is over a different surface
            reportFocus(wlPointer,
                        newFocus,
                        x,
                        y);
        }
        //update focus tracking
        this.focus = newFocus;

        if (this.grab.isPresent()
                && this.focus.equals(this.grab)) {
            reportMotion(wlPointer,
                         time,
                         x,
                         y);
        }
    }

    private void reportMotion(final WlPointer wlPointer,
                              final int time,
                              final int x,
                              final int y) {
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlPointer,
                                                                                this.grab.get()
                                                                                         .getResource()
                                                                                         .get());
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.compositor.getScene()
                                                                .relativeCoordinate(this.grab.get(),
                                                                                    x,
                                                                                    y);
            pointerResource.get()
                           .motion(time,
                                   Fixed.create(relativePoint.getX()),
                                   Fixed.create(relativePoint.getY()));
        }
    }

    private void reportEnter(final WlPointer wlPointer,
                             final int x,
                             final int y) {
        final WlSurfaceResource wlSurfaceResource = this.grab.get()
                                                             .getResource()
                                                             .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlPointer,
                                                                                wlSurfaceResource);
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.compositor.getScene()
                                                                .relativeCoordinate(this.grab.get(),
                                                                                    x,
                                                                                    y);
            pointerResource.get()
                           .enter(this.display.nextSerial(),
                                  wlSurfaceResource,
                                  Fixed.create(relativePoint.getX()),
                                  Fixed.create(relativePoint.getY()));
        }
    }

    private void reportLeave(final WlPointer wlPointer) {
        final WlSurfaceResource wlSurfaceResource = this.grab.get()
                                                             .getResource()
                                                             .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlPointer,
                                                                                wlSurfaceResource);
        if (pointerResource.isPresent()) {
            pointerResource.get()
                           .leave(this.display.nextSerial(),
                                  wlSurfaceResource);
        }
    }

    private void reportFocus(final WlPointer wlPointer,
                             final Optional<WlSurface> newFocus,
                             final int x,
                             final int y) {
        //the new focus does not match the 'old' focus.

        if (this.focus.isPresent()
                && this.grab.equals(this.focus)) {
            //a previous focus surface has the grab, report that the pointer left the surface
            reportLeave(wlPointer);
        }
        else if (newFocus.isPresent()
                && this.grab.equals(newFocus)) {
            //pointer is now over a surface that has the grab, report that it entered the surface
            reportEnter(wlPointer,
                        x,
                        y);
        }
    }

    private Optional<WlPointerResource> findPointerResource(final WlPointer wlPointer,
                                                            final WlSurfaceResource wlSurfaceResource) {
        for (final WlPointerResource wlPointerResource : wlPointer.getResources()) {
            if (wlSurfaceResource.getClient()
                                 .equals(wlPointerResource.getClient())) {
                return Optional.of(wlPointerResource);
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> getGrabSerial() {
        return this.grabSerial;
    }

}
