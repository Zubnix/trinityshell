package org.trinity.foundation.render.javafx.impl.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFutureTask;
import javafx.application.Platform;
import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.AbstractPropertySlotInvocationDelegate;

import javax.inject.Singleton;


@Bind
@Singleton
public class PropertySlotInvocationDelegateImpl extends AbstractPropertySlotInvocationDelegate {

    @Override
    protected void invokeSlot(final ListenableFutureTask<Void> invokeTask) {
        Platform.runLater(invokeTask);
    }
}
