package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_source;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;

import java.util.List;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlDataSource extends EventBus implements wl_data_source.Requests {

    final List<String> mimeTypes = Lists.newArrayList();

    WlDataSource() {
    }

    @Override
    public void offer(final wl_data_source.Resource resource,
                      final String                  mimeType) {
        this.mimeTypes.add(mimeType);
    }

    @Override
    public void destroy(final wl_data_source.Resource resource) {
        post(new ResourceDestroyed(resource));
    }
}
