package org.trinity.foundation.api.render.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFutureTask;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
*
*/
public abstract class AbstractPropertySlotInvocationDelegate implements PropertySlotInvocationDelegate {
    @Override
    public void invoke(final Object view, final Method viewMethod, final Object argument) {
        final ListenableFutureTask<Void> invokeTask = ListenableFutureTask.create(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                viewMethod.invoke(view,
                        argument);
                return null;
            }
        });
        invokeSlot(invokeTask);
    }

    protected abstract void invokeSlot(ListenableFutureTask<Void> invokeTask);
}
