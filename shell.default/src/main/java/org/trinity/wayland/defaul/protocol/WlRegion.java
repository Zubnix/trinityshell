package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_region;
import org.freedesktop.wayland.server.Client;
import org.trinity.shell.scene.api.Region;

import javax.annotation.Nonnegative;
import javax.media.nativewindow.util.Rectangle;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlRegionFactory")
public class WlRegion extends EventBus implements wl_region.Requests, ProtocolObject<wl_region.Resource> {

    private final Set<wl_region.Resource> resources = Sets.newHashSet();

    private final Region region;

    WlRegion(final Region region) {
        this.region = region;
    }

    @Override
    public Set<wl_region.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_region.Resource create(final Client client,
                                     final int version,
                                     final int id) {
        return new wl_region.Resource(client,
                                      version,
                                      id);
    }

    @Override
    public void destroy(final wl_region.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void add(final wl_region.Resource resource,
                    final int                x,
                    final int                y,
                    @Nonnegative final int   width,
                    @Nonnegative final int   height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        this.region.add(new Rectangle(x,
                                      y,
                                      width,
                                      height));
    }

    @Override
    public void subtract(final wl_region.Resource resource,
                         final int                x,
                         final int                y,
                         @Nonnegative final int   width,
                         @Nonnegative final int   height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        this.region.subtract(new Rectangle(x,
                                           y,
                                           width,
                                           height));
    }

    public Region getRegion(){
        return this.region;
    }
}
