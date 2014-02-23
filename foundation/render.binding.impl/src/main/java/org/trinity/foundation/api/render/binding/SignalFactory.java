package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListeningExecutorService;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

public interface SignalFactory {

    Signal create(ListeningExecutorService modelExecutor,
                  Object eventSignalReceiver,
                  String inputSlotName);
}
