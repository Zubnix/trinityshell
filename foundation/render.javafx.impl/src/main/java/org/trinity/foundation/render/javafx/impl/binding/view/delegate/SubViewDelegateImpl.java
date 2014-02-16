package org.trinity.foundation.render.javafx.impl.binding.view.delegate;

import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;
import org.trinity.foundation.render.javafx.api.FXView;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

/**
 *
 */
@Bind
@Singleton
public class SubViewDelegateImpl implements SubViewModelDelegate {

    private final Injector injector;

    @Inject
    SubViewDelegateImpl(final Injector injector) {
        this.injector = injector;
    }

    @Override
	public <T>T newView(final Object parentView,
										   final Class<T> childViewType,
										   final int position) {
		checkArgument(parentView instanceof Pane,
                      format("Expected parent view should be of type %s",
                             Pane.class.getName()));
        checkArgument(FXView.class.isAssignableFrom(childViewType),
                format("Expected child view should be of type %s",
                        FXView.class.getName()));

        return this.injector.getInstance(childViewType);
    }

    @Override
	public void destroyView(final Object parentView,
											  final Object deletedChildView,
											  final int deletedPosition) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //FIXME workaround for too eagerly registering view elements with a datacontext
                if (deletedChildView instanceof FXView) {
                    final FXView deletedChildViewInstance = (FXView) deletedChildView;
                    deletedChildViewInstance.close();
                }
            }
        });
    }

    @Override
	public void updateChildViewPosition(final Object parentView,
														  final Object childView,
														  final int oldPosition,
														  final int newPosition) {
		checkArgument(parentView instanceof FXView,
					  format("Expected parent view should be of type %s",
                        FXView.class.getName()));

        checkArgument(childView instanceof FXView,
                format("Expected child view should be of type %s",
                        FXView.class.getName()));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        });
    }
}
