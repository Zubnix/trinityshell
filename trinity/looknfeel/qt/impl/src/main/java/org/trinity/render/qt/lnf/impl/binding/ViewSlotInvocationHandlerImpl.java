package org.trinity.render.qt.lnf.impl.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.render.qt.api.QJRenderEngine;
import org.trinity.shellplugin.widget.api.binding.ViewAttribute;
import org.trinity.shellplugin.widget.api.binding.ViewSlotInvocationHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ViewSlotInvocationHandlerImpl implements ViewSlotInvocationHandler {

	private final QJRenderEngine qjRenderEngine;

	@Inject
	ViewSlotInvocationHandlerImpl(final QJRenderEngine qjRenderEngine) {
		this.qjRenderEngine = qjRenderEngine;
	}

	@Override
	public void invokeSlot(	final PaintableSurfaceNode paintableSurfaceNode,
							final ViewAttribute viewAttribute,
							final Object view,
							final Method viewAttributeSlot,
							final Object viewAttributeValue) {
		this.qjRenderEngine.invoke(	paintableSurfaceNode,
									new PaintInstruction<Void, QJPaintContext>() {
										@Override
										public Void call(final QJPaintContext paintContext) {
											invokeViewSlot(	paintContext,
															viewAttribute,
															view,
															viewAttributeSlot,
															viewAttributeValue);
											return null;

										};
									});
	}

	private void invokeViewSlot(final QJPaintContext paintContext,
								final ViewAttribute viewAttribute,
								final Object view,
								final Method viewAttributeSlot,
								final Object viewAttributeValue) {
		final Class<?>[] parameterTypes = viewAttributeSlot.getParameterTypes();
		final Object[] parameters = new Object[parameterTypes.length];
		for (int i = 0; i < parameters.length; i++) {
			final Class<?> parameterType = parameterTypes[i];

			if (parameterType.isAssignableFrom(QJPaintContext.class)) {
				parameters[i] = paintContext;
				continue;
			}

			if (parameterType.isAssignableFrom(ViewAttribute.class)) {
				parameters[i] = viewAttribute;
				continue;
			}

			if (parameterType.isAssignableFrom(viewAttributeValue.getClass())) {
				parameters[i] = viewAttributeValue;
				continue;
			}
		}

		try {
			viewAttributeSlot.invoke(	view,
										parameters);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}