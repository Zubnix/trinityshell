package org.trinity.foundation.api.render.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.lang.reflect.Method;

/**
 *
 */
public interface DefaultPropertySlotInvocationDelegate extends PropertySlotInvocationDelegate {
    @Override
    public default ListenableFuture<Void> invoke(final Object view, final Method viewMethod, final Object argument) {
        final ListenableFutureTask<Void> invokeTask = ListenableFutureTask.create(() -> {
            viewMethod.invoke(  view,
                                argument);
            return null;
        });
        invokeSlot(invokeTask);

        return invokeTask;
    }

    void invokeSlot(ListenableFutureTask<Void> invokeTask);
}