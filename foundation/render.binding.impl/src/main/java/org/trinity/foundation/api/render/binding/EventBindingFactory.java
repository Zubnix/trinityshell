package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.render.binding.view.EventSignal;

/**
 *
 */
public interface EventBindingFactory {

    EventBinding createEventBinding(ListeningExecutorService dataModelExecutor,
                                    ViewBindingMeta viewBindingMeta,
                                    EventSignal eventSignal);
}
