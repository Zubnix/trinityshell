package org.trinity.render.qt.impl.painter.instructions;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.render.api.PaintRoutine;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.render.qt.impl.QJRenderEventConverter;
import org.trinity.shell.api.widget.ShellWidget;
import org.trinity.shellplugin.widget.api.binding.BindingDiscovery;
import org.trinity.shellplugin.widget.api.binding.InputSignalDispatcher;
import org.trinity.shellplugin.widget.api.binding.ViewProperty;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.eventbus.EventBus;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGraphicsDropShadowEffect;
import com.trolltech.qt.gui.QWidget;

public class QJBindViewRoutine implements PaintRoutine<Void, QJPaintContext> {

	private final BindingDiscovery bindingDiscovery;
	private final ViewSlotInvocationHandler viewSlotInvocationHandler;
	private final QJRenderEventConverter renderEventConverter;
	private final EventBus displayEventBus;

	private final Optional<QWidget> parentView;
	private final QWidget view;
	private final Keyboard keyboard;

	public QJBindViewRoutine(	final BindingDiscovery bindingDiscovery,
								final ViewSlotInvocationHandler viewSlotInvocationHandler,
								final Keyboard keyboard,
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
		this.keyboard = keyboard;
	}

	@Override
	public Void call(final QJPaintContext paintContext) {
		checkArgument(paintContext.getPaintableSurfaceNode() instanceof ShellWidget);
		final ShellWidget shellWidget = (ShellWidget) paintContext.getPaintableSurfaceNode();
		initView(paintContext);
		bindViewProperties(	this.bindingDiscovery,
							this.viewSlotInvocationHandler,
							shellWidget,
							this.view);
		bindViewEventListeners(shellWidget);
		bindViewInputEmitters(shellWidget);

		return null;
	}

	private void bindViewEventListeners(final ShellWidget shellWidget) {
		new QJViewEventTracker(	this.displayEventBus,
								this.renderEventConverter,
								shellWidget,
								this.view);

	}

	private void bindViewInputEmitters(final ShellWidget shellWidget) {
		shellWidget.addShellNodeEventHandler(new InputSignalDispatcher(	shellWidget,
																		this.bindingDiscovery,
																		this.keyboard));

		new QJViewInputTracker(	this.displayEventBus,
								this.renderEventConverter,
								shellWidget,
								this.view);

	}

	private void initView(final QJPaintContext paintContext) {
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

		paintContext.syncVisualGeometryToSurfaceNode(this.view);

	}

	private void bindViewProperties(final BindingDiscovery bindingDiscovery,
									final ViewSlotInvocationHandler viewSlotInvocationHandler,
									final ShellWidget shellWidget,
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