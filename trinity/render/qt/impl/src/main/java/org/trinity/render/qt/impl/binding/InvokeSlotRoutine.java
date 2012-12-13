package org.trinity.render.qt.impl.binding;

import static com.google.common.base.Throwables.propagate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.render.api.PaintRoutine;
import org.trinity.render.qt.api.QJPaintContext;
import org.trinity.shellplugin.widget.api.binding.ViewProperty;

public class InvokeSlotRoutine implements PaintRoutine<Void, QJPaintContext> {

	private final ViewProperty viewProperty;
	private final Object view;
	private final Method viewSlot;
	private final Object argument;

	public InvokeSlotRoutine(	final ViewProperty viewProperty,
								final Object view,
								final Method viewSlot,
								final Object argument) {
		this.viewProperty = viewProperty;
		this.view = view;
		this.viewSlot = viewSlot;
		this.argument = argument;
	}

	@Override
	public Void call(QJPaintContext paintContext) {
		final Class<?>[] parameterTypes = viewSlot.getParameterTypes();
		final Object[] parameters = new Object[parameterTypes.length];
		for (int i = 0; i < parameters.length; i++) {
			final Class<?> parameterType = parameterTypes[i];

			if (parameterType.isAssignableFrom(QJPaintContext.class)) {
				parameters[i] = paintContext;
				continue;
			}

			if (parameterType.isAssignableFrom(ViewProperty.class)) {
				parameters[i] = viewProperty;
				continue;
			}

			if (parameterType.isAssignableFrom(argument.getClass())) {
				parameters[i] = argument;
				continue;
			}
		}

		try {
			viewSlot.invoke(view,
							parameters);
		} catch (final IllegalAccessException e) {
			propagate(e);
		} catch (final IllegalArgumentException e) {
			propagate(e);
		} catch (final InvocationTargetException e) {
			propagate(e);
		}
		return null;
	}
}
