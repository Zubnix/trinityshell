package org.trinity.wayland.protocol;

import com.google.common.base.Preconditions;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Listener;
import org.freedesktop.wayland.server.Resource;
import org.trinity.shell.scene.api.Listenable;
import org.trinity.wayland.protocol.events.ResourceDestroyed;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public interface ProtocolObject<T extends Resource<?>> extends Listenable {
    /**
     * Get all resources currently associated with this protocol object.
     *
     * @return All associated resources.
     */
    Set<T> getResources();

    /**
     * Get the only resource (if any) associated with this protocol object.
     * This method is a convenience method for protocol objects that should have
     * only a single resource associated with it.
     * It is an error to call this method if more than 1 resource is associated
     * with this protocol object.
     *
     * @return The only resource (if any) associated with this protocol object.
     */
    default Optional<T> getResource() {
        Preconditions.checkState(getResources().size() <= 1);

        final Iterator<T> iterator = getResources().iterator();
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Associate a new resource object with this protocol object.
     *
     * @param client  The client owning the newly created resource.
     * @param version The version desired by the client for the new resource.
     * @param id      The id for the new resource, as provided by the client
     * @return the newly created resource.
     */
    default T add(final Client client,
                  final int version,
                  final int id) {
        //FIXME check if version is supported by compositor.

        final T resource = create(client,
                                  version,
                                  id);
        resource.addDestroyListener(new Listener() {
            @Override
            public void handle() {
                remove();
                ProtocolObject.this.getResources()
                                   .remove(resource);
                ProtocolObject.this.post(new ResourceDestroyed(resource));
            }
        });
        getResources().add(resource);
        return resource;
    }

    /**
     * Create a resource.
     *
     * @param client  The client owning the newly created resource.
     * @param version The version desired by the client for the new resource.
     * @param id      The id for the new resource, as provided by the client
     * @return the newly created resource.
     */
    T create(final Client client,
             final int version,
             final int id);
}
