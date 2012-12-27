package org.trinity.foundation.render.qt.impl.painter;

import static com.google.common.base.Preconditions.checkNotNull;

import org.trinity.foundation.api.render.binding.BindingDiscovery;
import org.trinity.foundation.api.render.binding.ViewSlotInvocationHandler;
import org.trinity.foundation.render.qt.api.QJRenderEngine;
import org.trinity.foundation.render.qt.impl.QJRenderEventConverter;
import org.trinity.foundation.render.qt.impl.painter.routine.QJBindViewRoutine;

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

	@Inject
	QJViewBinder(	final QJRenderEngine qjRenderEngine,
					final BindingDiscovery bindingDiscovery,
					final ViewSlotInvocationHandler viewSlotInvocationHandler,
					@Named("displayEventBus") final EventBus displayEventBus,
					final QJRenderEventConverter qjRenderEventConverter) {
		this.qjRenderEngine = qjRenderEngine;
		this.bindingDiscovery = bindingDiscovery;
		this.viewSlotInvocationHandler = viewSlotInvocationHandler;
		this.displayEventBus = displayEventBus;
		this.qjRenderEventConverter = qjRenderEventConverter;
	}

	public void bindView(	final Optional<QWidget> parentView,
							final QWidget view,
							final Object dataContext) {
		checkNotNull(view);
		checkNotNull(dataContext);

		this.qjRenderEngine.invoke(	dataContext,
									new QJBindViewRoutine(	this.bindingDiscovery,
															this.viewSlotInvocationHandler,
															this.displayEventBus,
															this.qjRenderEventConverter,
															parentView,
															view));
	}
}