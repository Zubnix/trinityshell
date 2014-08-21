package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.WlDataDeviceManagerRequests;
import org.freedesktop.wayland.server.WlDataDeviceManagerResource;
import org.freedesktop.wayland.server.WlSeatResource;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//EAGER
public class WlDataDeviceManager extends Global implements WlDataDeviceManagerRequests, ProtocolObject<WlDataDeviceManagerResource> {

    private final Set<WlDataDeviceManagerResource> resources = Sets.newHashSet();
    private final EventBus                         eventBus  = new EventBus();

    private final WlDataSourceFactory wlDataSourceFactory;

    @Inject
    WlDataDeviceManager(final Display display,
                        final WlDataSourceFactory wlDataSourceFactory) {
        super(display,
              VERSION);
        this.wlDataSourceFactory = wlDataSourceFactory;
    }

    @Override
    public void onBindClient(final Client client,
                             final int version,
                             final int id) {
        add(client,
            version,
            id);
    }

    @Override
    public void createDataSource(final WlDataDeviceManagerResource resource,
                                 final int id) {
        this.wlDataSourceFactory.create().add(resource.getClient(),
                                              resource.getVersion(),
                                              id);
    }

    @Override
    public void getDataDevice(final WlDataDeviceManagerResource requester,
                              final int                         id,
                              final WlSeatResource              seat) {
        final WlSeat wlSeat = (WlSeat) seat.getImplementation();
        wlSeat.getWlDataDevice().add(requester.getClient(),
                                     requester.getVersion(),
                                     id);
    }

    @Override
    public Set<WlDataDeviceManagerResource> getResources() {
        return this.resources;
    }

    @Override
    public WlDataDeviceManagerResource create(final Client client,
                                                  final int    version,
                                                  final int    id) {
        return new WlDataDeviceManagerResource(client,
                                               version,
                                               id,
                                               this);
    }

    @Override
    public void register(@Nonnull final Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull final Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull final Object event) {
        this.eventBus.post(event);
    }
}
