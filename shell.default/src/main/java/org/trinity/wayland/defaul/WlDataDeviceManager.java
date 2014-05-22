package org.trinity.wayland.defaul;

import org.freedesktop.wayland.Interface;
import org.freedesktop.wayland.protocol.wl_data_device_manager;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//EAGER
public class WlDataDeviceManager extends Global implements wl_data_device_manager.Requests {

    @Inject
    WlDataDeviceManager(final Display display,
                        final Interface iface,
                        final int version) {
        super(display,
                iface,
                version);
    }

    @Override
    public void bindClient(final Client client,
                           final int version,
                           final int id) {
        new wl_data_device_manager.Resource(client,
                1,
                id).setImplementation(this);
    }

    @Override
    public void createDataSource(final wl_data_device_manager.Resource resource,
                                 final int id) {

    }

    @Override
    public void getDataDevice(final wl_data_device_manager.Resource resource,
                              final int id,
                              final org.freedesktop.wayland.server.Resource seat) {

    }
}
