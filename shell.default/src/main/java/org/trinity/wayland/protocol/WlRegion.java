package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlRegionRequests;
import org.freedesktop.wayland.server.WlRegionResource;
import org.trinity.shell.scene.api.Region;

import javax.annotation.Nonnegative;
import javax.media.nativewindow.util.Rectangle;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@AutoFactory(className = "WlRegionFactory")
public class WlRegion extends EventBus implements WlRegionRequests, ProtocolObject<WlRegionResource> {

    private final Set<WlRegionResource> resources = Sets.newHashSet();

    private final Region region;

    WlRegion(final Region region) {
        this.region = region;
    }

    @Override
    public Set<WlRegionResource> getResources() {
        return this.resources;
    }

    @Override
    public WlRegionResource create(final Client client,
                                   final int version,
                                   final int id) {
        return new WlRegionResource(client,
                                    version,
                                    id,
                                    this);
    }

    @Override
    public void destroy(final WlRegionResource resource) {
        resource.destroy();
    }

    @Override
    public void add(final WlRegionResource resource,
                    final int x,
                    final int y,
                    @Nonnegative final int width,
                    @Nonnegative final int height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        this.region.add(new Rectangle(x,
                                      y,
                                      width,
                                      height));
    }

    @Override
    public void subtract(final WlRegionResource resource,
                         final int x,
                         final int y,
                         @Nonnegative final int width,
                         @Nonnegative final int height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        this.region.subtract(new Rectangle(x,
                                           y,
                                           width,
                                           height));
    }

    public Region getRegion() {
        return this.region;
    }
}
