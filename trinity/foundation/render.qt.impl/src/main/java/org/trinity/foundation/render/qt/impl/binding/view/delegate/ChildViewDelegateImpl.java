package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.Renderer;
import org.trinity.foundation.api.render.binding.view.delegate.ChildViewDelegate;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QBoxLayout;
import com.trolltech.qt.gui.QFormLayout;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ChildViewDelegateImpl implements ChildViewDelegate {

	private final Injector injector;
	private final Renderer renderer;

	@Inject
	ChildViewDelegateImpl(final Injector injector, final Renderer renderer) {
		this.injector = injector;
		this.renderer = renderer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T newView(	final Object parentView,
							final Class<T> childViewType,
							final int position) throws ExecutionException {
		checkArgument(	parentView instanceof QWidget,
						format(	"Expected parent view should be of type %s",
								QWidget.class.getName()));
		checkArgument(	QWidget.class.isAssignableFrom(childViewType),
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		final QWidget parentViewInstance = (QWidget) parentView;

		final Future<T> newChildViewFuture = this.renderer.invoke(	this,
																	new PaintRoutine<T, PaintContext>() {
																		@Override
																		public T call(final PaintContext paintContext) {

																			final T childView = ChildViewDelegateImpl.this.injector
																					.getInstance(childViewType);
																			final QWidget childViewInstance = (QWidget) childView;

																			childViewInstance
																					.setParent(parentViewInstance);
																			final QLayout layout = parentViewInstance
																					.layout();
																			if (layout != null) {
																				addChildToLayout(	layout,
																									childViewInstance,
																									position);

																			}
																			return (T) childViewInstance;
																		}
																	});
		try {
			return newChildViewFuture.get();
		} catch (final InterruptedException e) {
			throw new ExecutionException(e);
		}
	}

	private void addChildToLayout(	final QLayout layout,
									final QWidget child,
									final int index) {
		if (layout instanceof QBoxLayout) {
			final QBoxLayout boxLayout = (QBoxLayout) layout;
			boxLayout.insertWidget(	index,
									child);
		} else if (layout instanceof QFormLayout) {
			final QFormLayout formLayout = (QFormLayout) layout;
			formLayout.insertRow(	index,
									child);
		} else if (layout instanceof QGridLayout) {
			final QGridLayout gridLayout = (QGridLayout) layout;
			gridLayout.addWidget(child);
		} else if (layout instanceof QStackedLayout) {
			final QStackedLayout stackedLayout = (QStackedLayout) layout;
			stackedLayout.insertWidget(	index,
										child);
		}
		layout.update();
	}

	@Override
	public void destroyView(final Object parentView,
							final Object deletedChildView,
							final int deletedPosition) {
		checkArgument(	deletedChildView instanceof QWidget,
						format(	"Expected child view should be of type %s",
								QWidget.class.getName()));

		this.renderer.invoke(	this,
								new PaintRoutine<Void, PaintContext>() {
									@Override
									public Void call(final PaintContext paintContext) throws ExecutionException {
										final QWidget deletedChildViewInstance = (QWidget) deletedChildView;
										deletedChildViewInstance.close();
										return null;
									}
								});
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

		final QWidget parentViewInstance = (QWidget) parentView;
		final QWidget childViewInstance = (QWidget) childView;

		this.renderer.invoke(	this,
								new PaintRoutine<Void, PaintContext>() {
									@Override
									public Void call(final PaintContext paintContext) throws ExecutionException {
										final QLayout layout = parentViewInstance.layout();
										if (layout != null) {
											layout.removeWidget(childViewInstance);
											addChildToLayout(	layout,
																childViewInstance,
																newPosition);

										}
										return null;
									}
								});
	}
}