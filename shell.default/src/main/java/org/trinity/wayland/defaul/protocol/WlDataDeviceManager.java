package org.trinity.wayland.defaul.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_device_manager;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//EAGER
public class WlDataDeviceManager extends Global implements wl_data_device_manager.Requests, ProtocolObject<wl_data_device_manager.Resource> {

    private final Set<wl_data_device_manager.Resource> resources = Sets.newHashSet();
    private final EventBus                             eventBus  = new EventBus();

    private final WlDataSourceFactory wlDataSourceFactory;

    @Inject
    WlDataDeviceManager(final Display display,
                        final WlDataSourceFactory wlDataSourceFactory) {
        super(display,
              wl_data_device_manager.WAYLAND_INTERFACE,
              1);
        this.wlDataSourceFactory = wlDataSourceFactory;
    }

    @Override
    public void bindClient(final Client client,
                           final int version,
                           final int id) {
        add(client,
            version,
            id);
    }

    @Override
    public void createDataSource(final wl_data_device_manager.Resource resource,
                                 final int id) {
        this.wlDataSourceFactory.create().add(resource.getClient(),
                                              1,
                                              id);
    }

    @Override
    public void getDataDevice(final wl_data_device_manager.Resource         resource,
                              final int                                     id,
                              final org.freedesktop.wayland.server.Resource seat) {
        final WlSeat wlSeat = (WlSeat) seat.getImplementation();
        wlSeat.getWlDataDevice().add(resource.getClient(),
                                     1,
                                     id);
    }

    @Override
    public Set<wl_data_device_manager.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_data_device_manager.Resource create(Client client,
                                                  int version,
                                                  int id) {
        return new wl_data_device_manager.Resource(client,
                                                   version,
                                                   id);
    }

    @Override
    public void register(@Nonnull Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull Object event) {
        this.eventBus.post(event);
    }
}
