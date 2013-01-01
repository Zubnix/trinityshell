package org.trinity.foundation.render.qt.impl.binding;

import static com.google.common.base.Throwables.propagate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;

public class InvokeSlotRoutine implements PaintRoutine<Void, PaintContext> {

	private final Object view;
	private final Method viewSlot;
	private final Object argument;

	public InvokeSlotRoutine(final Object view, final Method viewSlot, final Object argument) {
		this.view = view;
		this.viewSlot = viewSlot;
		this.argument = argument;
	}

	@Override
	public Void call(final PaintContext paintContext) {
		try {
			this.viewSlot.invoke(	this.view,
									this.argument);
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
