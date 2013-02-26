package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class PropertySlotInvocatorDelegateImpl implements PropertySlotInvocatorDelegate {

	private final PaintRenderer paintRenderer;

	@Inject
	PropertySlotInvocatorDelegateImpl(final PaintRenderer paintRenderer) {
		this.paintRenderer = paintRenderer;
	}

	@Override
	public void invoke(	final Object view,
						final Method viewMethod,
						final Object argument) {
		this.paintRenderer.invoke(	this,
									new PaintRoutine<Void, PaintContext>() {
										@Override
										public Void call(final PaintContext paintContext) throws ExecutionException {
											try {
												viewMethod.invoke(	view,
																	argument);
											} catch (final IllegalAccessException e) {
												throw new ExecutionException(e);
											} catch (final IllegalArgumentException e) {
												throw new ExecutionException(e);
											} catch (final InvocationTargetException e) {
												throw new ExecutionException(e);
											}
											return null;
										}
									});
	}
}
