package org.trinity.foundation.render.javafx.impl.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFutureTask;
import javafx.application.Platform;
import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.DefaultPropertySlotInvocationDelegate;

import javax.inject.Singleton;


@Bind
@Singleton
public class PropertySlotInvocationDelegateImpl implements DefaultPropertySlotInvocationDelegate {

    @Override
    public void invokeSlot(final ListenableFutureTask<Void> invokeTask) {
        Platform.runLater(invokeTask);
    }
}
