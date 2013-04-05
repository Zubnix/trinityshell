package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;
import static java.lang.String.format;

import static com.google.common.base.Preconditions.checkArgument;

@Bind
@Singleton
public class ChildViewDelegateImpl implements ChildViewDelegate {

	private final Injector injector;

	@Inject
	ChildViewDelegateImpl(final Injector injector) {
		this.injector = injector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ListenableFuture<T> newView(	final Object parentView,
											final Class<T> childViewType,
											final int position) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));
		checkArgument(	QWidget.class.isAssignableFrom(childViewType),
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final QWidget parentViewInstance = (QWidget) parentView;

		final Callable<T> routine = new Callable<T>() {
			@Override
			public T call() {

				final T childView = ChildViewDelegateImpl.this.injector.getInstance(childViewType);
				final QWidget childViewInstance = (QWidget) childView;

				childViewInstance.setParent(parentViewInstance);

				return (T) childViewInstance;
			}
		};
		final ListenableFutureTask<T> futureTask = ListenableFutureTask.create(routine);

		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> destroyView(	final Object parentView,
												final Object deletedChildView,
												final int deletedPosition) {
		checkArgument(	deletedChildView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final Callable<Void> routine = new Callable<Void>() {
			@Override
			public Void call() throws ExecutionException {
				final QWidget deletedChildViewInstance = (QWidget) deletedChildView;
				deletedChildViewInstance.close();
				return null;
			}
		};
		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(routine);

		QApplication.invokeLater(futureTask);
		return futureTask;
	}

	@Override
	public ListenableFuture<Void> updateChildViewPosition(	final Object parentView,
															final Object childView,
															final int oldPosition,
															final int newPosition) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));

		checkArgument(	childView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final Callable<Void> routine = new Callable<Void>() {
			@Override
			public Void call() throws ExecutionException {
				// FIXME only layouts have a notion of order in qt. How to
				// notify the layout the order of a child?
				return null;
			}
		};
		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(routine);

		QApplication.invokeLater(futureTask);
		return futureTask;
	}
}