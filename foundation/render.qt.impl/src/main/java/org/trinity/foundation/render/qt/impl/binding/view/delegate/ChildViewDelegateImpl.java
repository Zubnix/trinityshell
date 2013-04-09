package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

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
	public <T> T newView(	final Object parentView,
							final Class<T> childViewType,
							final int position) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));
		checkArgument(	QWidget.class.isAssignableFrom(childViewType),
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final QWidget parentViewInstance = (QWidget) parentView;

		final T childView = ChildViewDelegateImpl.this.injector.getInstance(childViewType);
		final QWidget childViewInstance = (QWidget) childView;

		childViewInstance.setParent(parentViewInstance);

		return (T) childViewInstance;
	}

	@Override
	public void destroyView(final Object parentView,
							final Object deletedChildView,
							final int deletedPosition) {
		checkArgument(	deletedChildView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final QWidget deletedChildViewInstance = (QWidget) deletedChildView;
		deletedChildViewInstance.close();
	}

	@Override
	public void updateChildViewPosition(final Object parentView,
										final Object childView,
										final int oldPosition,
										final int newPosition) {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));

		checkArgument(	childView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		// FIXME only layouts have a notion of order in qt. How to
		// notify the layout the order of a child?
	}
}