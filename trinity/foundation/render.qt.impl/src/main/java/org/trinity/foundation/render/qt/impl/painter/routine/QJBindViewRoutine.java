package org.trinity.foundation.render.qt.impl.painter.routine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.api.render.binding.ObservableCollection;
import org.trinity.foundation.api.render.binding.ObservedCollection;
import org.trinity.foundation.api.render.binding.ObservedCollectionHandler;
import org.trinity.foundation.api.render.binding.ViewSlotHandler;
import org.trinity.foundation.api.render.binding.model.ViewProperty;
import org.trinity.foundation.render.qt.impl.QJRenderEventConverter;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.eventbus.EventBus;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QWidget;

public class QJBindViewRoutine implements PaintRoutine<Void, PaintContext> {

	private final BindingDiscovery bindingDiscovery;
	private final Optional<ViewSlotHandler> optionalViewSlotInvocationHandler;
	private final Optional<ObservedCollectionHandler> optionalObservableCollectionHandler;
	private final QJRenderEventConverter renderEventConverter;
	private final EventBus displayEventBus;

	private final Optional<QWidget> parentView;
	private final QWidget view;

	public QJBindViewRoutine(	final BindingDiscovery bindingDiscovery,
								final Optional<ViewSlotHandler> viewSlotHandler,
								final Optional<ObservedCollectionHandler> observedCollectionHandler,
								final EventBus displayEventBus,
								final QJRenderEventConverter renderEventConverter,
								final Optional<QWidget> parentView,
								final QWidget view) {

		this.bindingDiscovery = bindingDiscovery;
		this.optionalViewSlotInvocationHandler = viewSlotHandler;
		this.optionalObservableCollectionHandler = observedCollectionHandler;

		this.renderEventConverter = renderEventConverter;
		this.displayEventBus = displayEventBus;

		this.parentView = parentView;
		this.view = view;
	}

	@Override
	public Void call(final PaintContext paintContext) {
		final Object rootDataContext = paintContext.getDataContext();
		initView(paintContext);
		bindViewProperties(rootDataContext);
		bindViewEventListeners(rootDataContext);
		bindViewInputEmitters(rootDataContext);
		bindObservedCollections(rootDataContext);

		return null;
	}

	private void bindObservedCollections(final Object dataContext) {
		if (!this.optionalObservableCollectionHandler.isPresent()) {
			return;
		}

		try {
			final Method[] observableCollections = this.bindingDiscovery.lookupAllObservableCollections(dataContext
					.getClass());
			for (final Method method : observableCollections) {
				final ObservableCollection observableCollection = method.getAnnotation(ObservableCollection.class);
				final Optional<Method> observedCollection = this.bindingDiscovery
						.lookupObservedCollection(	this.view.getClass(),
													observableCollection.name());
				if (!observedCollection.isPresent()) {
					continue;
				}
				final ObservedCollection observedCollectionMeta = observedCollection.get()
						.getAnnotation(ObservedCollection.class);
				final EventList<?> collection = (EventList<?>) method.invoke(dataContext);
				collection.addListEventListener(new ListEventListener<Object>() {

					@Override
					public void listChanged(final ListEvent<Object> listChanges) {

						// TODO Auto-generated method stub

					}

				});
				this.optionalObservableCollectionHandler.get().handleObservedCollection(dataContext,
																						this.view,
																						observedCollectionMeta,
																						observedCollection.get(),
																						collection);
			}
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}

	private void bindViewEventListeners(final Object dataContext) {
		new QJViewEventTracker(	this.displayEventBus,
								this.renderEventConverter,
								dataContext,
								this.view);

	}

	private void bindViewInputEmitters(final Object dataContext) {
		new QJViewInputTracker(	this.displayEventBus,
								this.renderEventConverter,
								dataContext,
								this.view);

	}

	private void initView(final PaintContext paintContext) {
		if (this.parentView.isPresent()) {
			this.view.setParent(this.parentView.get());
		}

		final QGraphicsDropShadowEffect effect = new QGraphicsDropShadowEffect();
		effect.setBlurRadius(10);
		effect.setOffset(	0,
							5);
		effect.setColor(QColor.darkGray);
		this.view.setGraphicsEffect(effect);

		this.view.setWindowFlags(WindowType.X11BypassWindowManagerHint);
		this.view.setAttribute(	WidgetAttribute.WA_DeleteOnClose,
								true);
		this.view.setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
								true);
	}

	private void bindViewProperties(final Object dataContext) {
		if (!this.optionalViewSlotInvocationHandler.isPresent()) {
			return;
		}

		try {
			final Method[] viewProperties = this.bindingDiscovery.lookupAllViewProperties(dataContext.getClass());
			for (final Method method : viewProperties) {
				final ViewProperty viewProperty = method.getAnnotation(ViewProperty.class);
				final Optional<Method> viewSlot = this.bindingDiscovery.lookupViewPropertySlot(	this.view.getClass(),
																								viewProperty.value());
				if (!viewSlot.isPresent()) {
					continue;
				}

				final Object argument = method.invoke(dataContext);
				this.optionalViewSlotInvocationHandler.get().invokeSlot(dataContext,
																		this.view,
																		viewSlot.get(),
																		argument);
			}
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}
}