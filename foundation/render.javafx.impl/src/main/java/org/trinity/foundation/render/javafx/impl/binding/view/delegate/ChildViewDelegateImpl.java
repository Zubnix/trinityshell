package org.trinity.foundation.render.javafx.impl.binding.view.delegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

/**
 *
 */
@Bind
@Singleton
public class ChildViewDelegateImpl implements ChildViewDelegate {

    private final Injector injector;

    @Inject
    ChildViewDelegateImpl(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public <T> ListenableFuture<T> newView(final Object parentView, final Class<T> childViewType, final int position) {
        checkArgument(parentView instanceof Pane,
                      format("Expected parent view should be of type %s",
                             Pane.class.getName()));
        checkArgument(FXView.class.isAssignableFrom(childViewType),
                format("Expected child view should be of type %s",
                        FXView.class.getName()));

        final ListenableFutureTask<T> newViewTask = ListenableFutureTask.create(new Callable<T>() {
            @Override
            public T call() throws Exception {
                final Pane parentViewInstance = (Pane) parentView;

                final T childView = ChildViewDelegateImpl.this.injector.getInstance(childViewType);
                final FXView childViewInstance = (FXView) childView;

                childViewInstance.setParent(parentViewInstance);


                return (T) childViewInstance;
            }
        });

        Platform.runLater(newViewTask);

        return newViewTask;
    }

    @Override
    public ListenableFuture<Void> destroyView(final Object parentView, final Object deletedChildView, final int deletedPosition) {

        final ListenableFutureTask<Void> destroyTask = ListenableFutureTask.create(new Callable<Void>() {
            @Override
            public Void call() {
                //FIXME workaround for too eagerly registering view elements with a datacontext
                if (deletedChildView instanceof FXView) {
                    final FXView deletedChildViewInstance = (FXView) deletedChildView;
                    deletedChildViewInstance.close();
                }
                return null;
            }
        });

        Platform.runLater(destroyTask);
        return destroyTask;
    }

    @Override
    public ListenableFuture<Void> updateChildViewPosition(final Object parentView, final Object childView, final int oldPosition, final int newPosition) {
        checkArgument(parentView instanceof FXView,
                format("Expected parent view should be of type %s",
                        FXView.class.getName()));

        checkArgument(childView instanceof FXView,
                format("Expected child view should be of type %s",
                        FXView.class.getName()));

        final ListenableFutureTask<Void> updateChildViewPosTask = ListenableFutureTask.create(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // TODO
                return null;
            }
        });

        Platform.runLater(updateChildViewPosTask);
        return updateChildViewPosTask;
    }
}
