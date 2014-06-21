package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_source;
import org.freedesktop.wayland.server.Client;

import java.util.List;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlDataSourceFactory")
public class WlDataSource extends EventBus implements wl_data_source.Requests, ProtocolObject<wl_data_source.Resource> {

    private final Set<wl_data_source.Resource> resources = Sets.newHashSet();
    private final List<String>                 mimeTypes = Lists.newArrayList();

    WlDataSource() {
    }

    @Override
    public void offer(final wl_data_source.Resource resource,
                      final String mimeType) {
        this.mimeTypes.add(mimeType);
    }

    @Override
    public Set<wl_data_source.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_data_source.Resource create(final Client client,
                                          final int    version,
                                          final int    id) {
        return new wl_data_source.Resource(client,
                                           version,
                                           id);
    }

    @Override
    public void destroy(final wl_data_source.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }
}
