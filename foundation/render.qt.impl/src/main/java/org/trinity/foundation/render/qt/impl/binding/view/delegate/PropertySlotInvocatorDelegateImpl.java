package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class PropertySlotInvocatorDelegateImpl implements PropertySlotInvocatorDelegate {

	@Inject
	PropertySlotInvocatorDelegateImpl() {
	}

	@Override
	public ListenableFuture<Void> invoke(	final Object view,
											final Method viewMethod,
											final Object argument) {

		final Callable<Void> routine = new Callable<Void>() {
			@Override
			public Void call() throws ExecutionException {
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
		};
		final ListenableFutureTask<Void> futureTask = ListenableFutureTask.create(routine);

		QApplication.invokeLater(futureTask);
		return futureTask;
	}
}
