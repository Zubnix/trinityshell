package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlDataSourceRequests;
import org.freedesktop.wayland.server.WlDataSourceResource;

import java.util.List;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlDataSourceFactory")
public class WlDataSource extends EventBus implements WlDataSourceRequests, ProtocolObject<WlDataSourceResource> {

    private final Set<WlDataSourceResource> resources = Sets.newHashSet();
    private final List<String>              mimeTypes = Lists.newArrayList();

    WlDataSource() {
    }

    @Override
    public void offer(final WlDataSourceResource resource,
                      final String mimeType) {
        this.mimeTypes.add(mimeType);
    }

    @Override
    public Set<WlDataSourceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlDataSourceResource create(final Client client,
                                       final int    version,
                                       final int    id) {
        return new WlDataSourceResource(client,
                                        version,
                                        id,
                                        this);
    }

    @Override
    public void destroy(final WlDataSourceResource resource) {
        ProtocolObject.super.destroy(resource);
    }
}
