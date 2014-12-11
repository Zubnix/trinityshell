package org.trinity.wayland.protocol;

import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;
import org.trinity.shell.scene.api.Listenable;
import org.trinity.wayland.protocol.events.ResourceDestroyed;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
public interface ProtocolObject<T extends Resource> extends Listenable {
    Set<T> getResources();

    default Optional<T> getResource(){
      final Iterator<T> iterator = getResources().iterator();
      if(iterator.hasNext()){
        return Optional.of(iterator.next());
      }else{
        return Optional.empty();
      }
    }

    default T add(final Client client,
                  final int    version,
                  final int    id){
        //FIXME check if version is supported by compositor.

        final T resource = create(client,
                                  version,
                                  id);
        getResources().add(resource);
        return  resource;
    }

    T create(final Client client,
             final int version,
             final int id);

    default void destroy(final T resource){
        post(new ResourceDestroyed(resource));
        getResources().remove(resource);
        resource.destroy();
    }
}
