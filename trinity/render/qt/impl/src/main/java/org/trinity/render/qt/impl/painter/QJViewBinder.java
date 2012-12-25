package org.trinity.render.qt.impl.painter;

import static com.google.common.base.Preconditions.checkNotNull;

import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJRenderEngine;
import org.trinity.render.qt.impl.QJRenderEventConverter;
import org.trinity.render.qt.impl.painter.instructions.QJBindViewRoutine;
import org.trinity.shellplugin.widget.api.binding.BindingDiscovery;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
public class QJViewBinder {

	private final QJRenderEngine qjRenderEngine;
	private final BindingDiscovery bindingDiscovery;
	private final ViewSlotInvocationHandler viewSlotInvocationHandler;
	private final EventBus displayEventBus;
	private final QJRenderEventConverter qjRenderEventConverter;
	private final Keyboard keyboard;

	@Inject
	QJViewBinder(	final QJRenderEngine qjRenderEngine,
					final BindingDiscovery bindingDiscovery,
					final ViewSlotInvocationHandler viewSlotInvocationHandler,
					@Named("displayEventBus") final EventBus displayEventBus,
					final QJRenderEventConverter qjRenderEventConverter,
					final Keyboard keyboard) {
		this.qjRenderEngine = qjRenderEngine;
		this.bindingDiscovery = bindingDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
		this.displayEventBus = displayEventBus;
		this.qjRenderEventConverter = qjRenderEventConverter;
		this.keyboard = keyboard;
	}

	public void bindView(	final Optional<QWidget> parentView,
							final QWidget view,
							final PaintableSurfaceNode paintableSurfaceNode) {
		checkNotNull(view);
		checkNotNull(paintableSurfaceNode);

		this.qjRenderEngine.invoke(	paintableSurfaceNode,
									new QJBindViewRoutine(	this.bindingDiscovery,
															this.viewSlotInvocationHandler,
															this.keyboard,
															this.displayEventBus,
															this.qjRenderEventConverter,
															parentView,
															view));
	}
}