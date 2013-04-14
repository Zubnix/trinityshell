package org.trinity.foundation.render.qt.impl.binding.view.delegate;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocatorDelegate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Singleton;
import com.trolltech.qt.gui.QApplication;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class PropertySlotInvocatorDelegateImpl implements PropertySlotInvocatorDelegate {

	PropertySlotInvocatorDelegateImpl() {
	}

	@Override
	public ListenableFuture<Void> invoke(	final Object view,
											final Method viewMethod,
											final Object argument) {
		final ListenableFutureTask<Void> invokeTask = ListenableFutureTask.create(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				viewMethod.invoke(	view,
									argument);
				return null;
			}
		});

		QApplication.invokeLater(invokeTask);
		return invokeTask;
	}
}
