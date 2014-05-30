package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_region;
import org.pixman.pixman_region32;
import org.trinity.common.Listenable;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;

import javax.annotation.Nonnegative;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlRegion extends EventBus implements wl_region.Requests, Listenable {

    private pixman_region32 pixman_region32 = new pixman_region32();

    WlRegion() {
    }


    @Override
    public void destroy(final wl_region.Resource resource) {
        post(new ResourceDestroyed(resource));
        resource.destroy();
    }

    @Override
    public void add(final wl_region.Resource resource,
                    final int x,
                    final int y,
                    @Nonnegative final int width,
                    @Nonnegative final int height) {
        checkArgument(width > 0);
        checkArgument(height > 0);


    }

    @Override
    public void subtract(final wl_region.Resource resource,
                         final int x,
                         final int y,
                         @Nonnegative final int width,
                         @Nonnegative final int height) {
        checkArgument(width > 0);
        checkArgument(height > 0);


    }

    public pixman_region32 getPixman_region32() {
        return this.pixman_region32;
    }
}
