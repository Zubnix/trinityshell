package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton//EAGER
public class WlDataDeviceManager extends Global<WlDataDeviceManagerResource> implements WlDataDeviceManagerRequests, ProtocolObject<WlDataDeviceManagerResource> {

    private final Set<WlDataDeviceManagerResource> resources = Sets.newHashSet();
    private final EventBus                         eventBus  = new EventBus();

    private final WlDataSourceFactory wlDataSourceFactory;

    @Inject
    WlDataDeviceManager(final Display display,
                        final WlDataSourceFactory wlDataSourceFactory) {
        super(display,
              WlDataDeviceManagerResource.class,
              VERSION);
        this.wlDataSourceFactory = wlDataSourceFactory;
    }

    @Override
    public WlDataDeviceManagerResource onBindClient(final Client client,
                                                    final int version,
                                                    final int id) {
        return add(client,
                   version,
                   id);
    }

    @Override
    public void createDataSource(final WlDataDeviceManagerResource resource,
                                 final int id) {
        this.wlDataSourceFactory.create()
                                .add(resource.getClient(),
                                     resource.getVersion(),
                                     id);
    }

    @Override
    public void getDataDevice(final WlDataDeviceManagerResource requester,
                              final int id,
                              @Nonnull final WlSeatResource seat) {
        final WlSeat wlSeat = (WlSeat) seat.getImplementation();
//        wlSeat.getWlDataDevice()
//              .add(requester.getClient(),
//                   requester.getVersion(),
//                   id);
    }

    @Override
    public Set<WlDataDeviceManagerResource> getResources() {
        return this.resources;
    }

    @Override
    public WlDataDeviceManagerResource create(final Client client,
                                              final int version,
                                              final int id) {
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
