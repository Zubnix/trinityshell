package org.trinity.foundation.render.qt.impl.binding;

import static com.google.common.base.Throwables.propagate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.ViewProperty;

public class InvokeSlotRoutine implements PaintRoutine<Void, PaintContext> {

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
	public Void call(final PaintContext paintContext) {
		final Class<?>[] parameterTypes = this.viewSlot.getParameterTypes();
		final Object[] parameters = new Object[parameterTypes.length];
		for (int i = 0; i < parameters.length; i++) {
			final Class<?> parameterType = parameterTypes[i];

			if (parameterType.isAssignableFrom(PaintContext.class)) {
				parameters[i] = paintContext;
				continue;
			}

			if (parameterType.isAssignableFrom(ViewProperty.class)) {
				parameters[i] = this.viewProperty;
				continue;
			}

			if (parameterType.isAssignableFrom(this.argument.getClass())) {
				parameters[i] = this.argument;
				continue;
			}
		}

		try {
			this.viewSlot.invoke(	this.view,
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
