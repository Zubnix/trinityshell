package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.error.BindingError;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ChildViewDelegateImpl implements ChildViewDelegate {

	private final Injector injector;
	private final PaintRenderer paintRenderer;

	@Inject
	ChildViewDelegateImpl(final Injector injector,
			final PaintRenderer paintRenderer) {
		this.injector = injector;
		this.paintRenderer = paintRenderer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T newView(final Object parentView, final Class<T> childViewType,
			final int position) {
		checkArgument(
				parentView instanceof QWidget,
				format("Expected parent view should be of type %s",
						QWidget.class.getName()));
		checkArgument(
				QWidget.class.isAssignableFrom(childViewType),
				format("Expected child view should be of type %s",
						QWidget.class.getName()));

		final QWidget parentViewInstance = (QWidget) parentView;

		final PaintRoutine<T, PaintContext> newChildViewRoutine = new PaintRoutine<T, PaintContext>() {
			@Override
			public T call(final PaintContext paintContext) {

				final T childView = ChildViewDelegateImpl.this.injector
						.getInstance(childViewType);
				final QWidget childViewInstance = (QWidget) childView;

				childViewInstance.setParent(parentViewInstance);

				// FIXME only layouts have a notion of order in qt. How to
				// notify the layout the order of a child?

				return (T) childViewInstance;
			}
		};

		final Future<T> newChildViewFuture = this.paintRenderer.invoke(this,
				newChildViewRoutine);
		try {
			return newChildViewFuture.get();
		} catch (final InterruptedException e) {
			throw new BindingError("", e);
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
			return null;
		}
	}

	@Override
	public void destroyView(final Object parentView,
			final Object deletedChildView, final int deletedPosition) {
		checkArgument(
				deletedChildView instanceof QWidget,
				format("Expected child view should be of type %s",
						QWidget.class.getName()));

		final PaintRoutine<Void, PaintContext> destroyChildViewRoutine = new PaintRoutine<Void, PaintContext>() {
			@Override
			public Void call(final PaintContext paintContext)
					throws ExecutionException {
				final QWidget deletedChildViewInstance = (QWidget) deletedChildView;
				deletedChildViewInstance.close();
				return null;
			}
		};

		this.paintRenderer.invoke(this, destroyChildViewRoutine);
	}

	@Override
	public void updateChildViewPosition(final Object parentView,
			final Object childView, final int oldPosition, final int newPosition) {
		checkArgument(
				parentView instanceof QWidget,
				format("Expected parent view should be of type %s",
						QWidget.class.getName()));

		checkArgument(
				childView instanceof QWidget,
				format("Expected child view should be of type %s",
						QWidget.class.getName()));

		// FIXME only layouts have a notion of order in qt. How to notify the
		// layout the order of a child?
	}
}