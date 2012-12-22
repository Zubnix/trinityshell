package org.trinity.render.qt.impl.painter;

import static com.google.common.base.Preconditions.checkNotNull;

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
	private BindingDiscovery bindingDiscovery;
	private ViewSlotInvocationHandler viewSlotInvocationHandler;
	private EventBus displayEventBus;
	private QJRenderEventConverter qjRenderEventConverter;

	@Inject
	QJViewBinder(	QJRenderEngine qjRenderEngine,
						BindingDiscovery bindingDiscovery,
						ViewSlotInvocationHandler viewSlotInvocationHandler,
						@Named("displayEventBus") EventBus displayEventBus,
						QJRenderEventConverter qjRenderEventConverter) {
		this.qjRenderEngine = qjRenderEngine;
		this.bindingDiscovery = bindingDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
		this.displayEventBus = displayEventBus;
		this.qjRenderEventConverter = qjRenderEventConverter;
	}

	public void bindView(	Optional<QWidget> parentView,
							QWidget view,
							PaintableSurfaceNode paintableSurfaceNode) {
		checkNotNull(view);
		checkNotNull(paintableSurfaceNode);

		this.qjRenderEngine.invoke(	paintableSurfaceNode,
									new QJBindViewRoutine(	bindingDiscovery,
															viewSlotInvocationHandler,
															displayEventBus,
															qjRenderEventConverter,
															parentView,
															view));
	}
}