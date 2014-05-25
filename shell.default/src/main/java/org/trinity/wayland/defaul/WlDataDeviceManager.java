package org.trinity.wayland.defaul;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.protocol.wl_data_device_manager;
import org.freedesktop.wayland.protocol.wl_data_source;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//EAGER
public class WlDataDeviceManager extends Global implements wl_data_device_manager.Requests {

    private final WlDataSourceFactory wlDataSourceFactory;

    @Inject
    WlDataDeviceManager(final Display               display,
                        final WlDataSourceFactory   wlDataSourceFactory) {
        super(display,
              wl_data_device_manager.WAYLAND_INTERFACE,
              1);
        this.wlDataSourceFactory = wlDataSourceFactory;
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        new wl_data_device_manager.Resource(client,
                                            1,
                                            id).setImplementation(this);
    }

    @Override
    public void createDataSource(final wl_data_device_manager.Resource  resource,
                                 final int                              id) {
        final WlDataSource wlDataSource                     = this.wlDataSourceFactory.create();
        final wl_data_source.Resource dataSourceResource    = new wl_data_source.Resource(resource.getClient(),
                                                                                          1,
                                                                                          id);
        dataSourceResource.setImplementation(wlDataSource);
        wlDataSource.register(new Object(){
            @Subscribe
            public void handle(final ResourceDestroyed event){
                wlDataSource.unregister(this);
                dataSourceResource.destroy();
            }
        });
    }

    @Override
    public void getDataDevice(final wl_data_device_manager.Resource         resource,
                              final int                                     id,
                              final org.freedesktop.wayland.server.Resource seat) {

    }
}
