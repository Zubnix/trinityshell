package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.*;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.freedesktop.wayland.util.Fixed;
import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.output.events.Button;
import org.trinity.wayland.output.events.Motion;

import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;
import java.util.Set;

@AutoFactory(className = "WlPointerFactory")
public class WlPointer extends EventBus implements WlPointerRequestsV3, ProtocolObject<WlPointerResource> {

    private final Set<WlPointerResource> resources = Sets.newHashSet();

    private final Display    display;
    private final Compositor compositor;

    private Optional<WlSurface> grab       = Optional.empty();
    private Optional<Integer>   grabSerial = Optional.empty();
    private Optional<WlSurface> focus      = Optional.empty();

    private int buttonsPressed;

    WlPointer(@Provided final Display display,
              final Compositor compositor) {
        this.display = display;
        this.compositor = compositor;
    }

    @Override
    public void release(final WlPointerResource resource) {

    }

    @Override
    public void setCursor(final WlPointerResource requester,
                          final int serial,
                          final WlSurfaceResource surface,
                          final int hotspotX,
                          final int hotspotY) {

    }

    @Override
    public Set<WlPointerResource> getResources() {
        return this.resources;
    }

    @Override
    public WlPointerResource create(final Client client,
                                    final int version,
                                    final int id) {
        return new WlPointerResource(client,
                                     version,
                                     id,
                                     this);
    }

    @Subscribe
    public void handle(final Button button) {
        button(button.getTime(),
               button.getButton(),
               button.getButtonState());
    }

    private void button(final int time,
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
            reportButton(this.grabSerial.get(),
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
            reportButton(serial,
                         time,
                         button,
                         buttonState);
        }
    }

    private void reportButton(final int serial,
                              final int time,
                              final int button,
                              final WlPointerButtonState buttonState) {
        final Optional<WlSurfaceResource> wlSurfaceResource = this.grab.get()
                                                                       .getResource();
        if (wlSurfaceResource.isPresent()) {
            final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource.get());
            if (pointerResource.isPresent()) {
                pointerResource.get()
                               .button(serial,
                                       time,
                                       button,
                                       buttonState.getValue());
            }
        }
    }

    @Subscribe
    public void handle(final Motion motion) {
        move(motion.getTime(),
             motion.getX(),
             motion.getY());
    }


    private void move(final int time,
                      final int x,
                      final int y) {

        final Optional<WlSurface> newFocus = this.compositor.getScene()
                                                            .findSurfaceAtCoordinate(x,
                                                                                     y);
        if (this.grab.isPresent() && !this.focus.equals(newFocus)) {
            //pointer is over a different surface
            reportFocus(newFocus,
                        x,
                        y);
        }
        //update focus tracking
        this.focus = newFocus;

        if (this.grab.isPresent()
                && this.focus.equals(this.grab)) {
            reportMotion(time,
                         x,
                         y);
        }
    }

    private void reportMotion(final int time,
                              final int x,
                              final int y) {
        final Optional<WlPointerResource> pointerResource = findPointerResource(this.grab.get()
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

    private void reportEnter(final int x,
                             final int y) {
        final WlSurfaceResource wlSurfaceResource = this.grab.get()
                                                             .getResource()
                                                             .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
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

    private void reportLeave() {
        final WlSurfaceResource wlSurfaceResource = this.grab.get()
                                                             .getResource()
                                                             .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            pointerResource.get()
                           .leave(this.display.nextSerial(),
                                  wlSurfaceResource);
        }
    }

    private void reportFocus(final Optional<WlSurface> newFocus,
                             final int x,
                             final int y) {
        //the new focus does not match the 'old' focus.

        if (this.focus.isPresent()
                && this.grab.equals(this.focus)) {
            //a previous focus surface has the grab, report that the pointer left the surface
            reportLeave();
        }
        else if (newFocus.isPresent()
                && this.grab.equals(newFocus)) {
            //pointer is now over a surface that has the grab, report that it entered the surface
            reportEnter(x,
                        y);
        }
    }

    private Optional<WlPointerResource> findPointerResource(final WlSurfaceResource wlSurfaceResource) {
        for (final WlPointerResource wlPointerResource : getResources()) {
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
