package org.trinity.foundation.render.qt.impl.painter.routine;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.event.DisplayEventTarget;
import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.api.render.binding.ViewProperty;
import org.trinity.foundation.api.render.binding.ViewSlotInvocationHandler;
import org.trinity.foundation.render.qt.impl.QJRenderEventConverter;

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
	private final ViewSlotInvocationHandler viewSlotInvocationHandler;
	private final QJRenderEventConverter renderEventConverter;
	private final EventBus displayEventBus;

	private final Optional<QWidget> parentView;
	private final QWidget view;

	public QJBindViewRoutine(	final BindingDiscovery bindingDiscovery,
								final ViewSlotInvocationHandler viewSlotInvocationHandler,
								final EventBus displayEventBus,
								final QJRenderEventConverter renderEventConverter,
								final Optional<QWidget> parentView,
								final QWidget view) {

		this.bindingDiscovery = bindingDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
		this.renderEventConverter = renderEventConverter;
		this.displayEventBus = displayEventBus;

		this.parentView = parentView;
		this.view = view;
	}

	@Override
	public Void call(final PaintContext paintContext) {
		final Object shellWidget = paintContext.getDataContext();
		checkArgument(shellWidget instanceof DisplayEventTarget);
		initView(paintContext);
		bindViewProperties(	this.bindingDiscovery,
							this.viewSlotInvocationHandler,
							shellWidget,
							this.view);
		bindViewEventListeners((DisplayEventTarget) shellWidget);
		bindViewInputEmitters((DisplayEventTarget) shellWidget);

		return null;
	}

	private void bindViewEventListeners(final DisplayEventTarget dataContext) {
		new QJViewEventTracker(	this.displayEventBus,
								this.renderEventConverter,
								dataContext,
								this.view);

	}

	private void bindViewInputEmitters(final DisplayEventTarget dataContext) {
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

	private void bindViewProperties(final BindingDiscovery bindingDiscovery,
									final ViewSlotInvocationHandler viewSlotInvocationHandler,
									final Object shellWidget,
									final Object view) {
		Method[] fields;

		try {
			fields = bindingDiscovery.lookupAllViewProperties(shellWidget.getClass());
			for (final Method method : fields) {
				final ViewProperty viewProperty = method.getAnnotation(ViewProperty.class);
				final Optional<Method> viewSlot = bindingDiscovery.lookupViewPropertySlot(	view.getClass(),
																							viewProperty.value());
				if (!viewSlot.isPresent()) {
					continue;
				}

				final Object argument = method.invoke(shellWidget);
				viewSlotInvocationHandler.invokeSlot(	shellWidget,
														viewProperty,
														view,
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