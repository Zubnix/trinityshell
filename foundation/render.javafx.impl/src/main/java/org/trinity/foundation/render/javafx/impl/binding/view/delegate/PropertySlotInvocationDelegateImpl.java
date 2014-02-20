package org.trinity.foundation.render.javafx.impl.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFutureTask;
import javafx.application.Platform;
import org.trinity.foundation.api.render.binding.view.delegate.AbstractPropertySlotInvocationDelegate;

import javax.inject.Singleton;

@Singleton
public class PropertySlotInvocationDelegateImpl extends AbstractPropertySlotInvocationDelegate {

    @Override
    protected void invokeSlot(final ListenableFutureTask<Void> invokeTask) {
        Platform.runLater(invokeTask);
    }
}