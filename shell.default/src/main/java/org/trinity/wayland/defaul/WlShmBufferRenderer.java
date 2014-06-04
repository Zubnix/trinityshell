package org.trinity.wayland.defaul;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
public interface WlShmBufferRenderer {

    static EventBus DISPATCHER(final WlShmBuffer wlShmBuffer){
        return new EventBus(){{
            register(new Object(){
                @Subscribe
                public void dispatch(final WlShmBufferRenderer renderer){
                    renderer.visit(wlShmBuffer);
                }
            });
        }};
    }

    void visit(WlShmBuffer wlShmBuffer);
}
