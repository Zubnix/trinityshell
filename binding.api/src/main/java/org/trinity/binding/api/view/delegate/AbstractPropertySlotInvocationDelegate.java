package org.trinity.binding.api.view.delegate;

import com.google.common.util.concurrent.ListenableFutureTask;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 *
 */
public abstract class AbstractPropertySlotInvocationDelegate implements PropertySlotInvocationDelegate {
    @Override
    public void invoke(@Nonnull final Object view,
                       @Nonnull final Method viewMethod,
                       @Nonnull final Object argument) {
        final ListenableFutureTask<Void> invokeTask = ListenableFutureTask.create(() -> {
            viewMethod.invoke(view,
                              argument);
            return null;
        });
        invokeSlot(invokeTask);
    }

    protected abstract void invokeSlot(@Nonnull ListenableFutureTask<Void> invokeTask);
}
