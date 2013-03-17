package org.trinity.shellplugin.widget.impl.view.qt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.inject.Provider;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractQWidgetViewProvider<T extends QWidget> implements Provider<T> {

	protected AbstractQWidgetViewProvider() {
	}

	@Override
	public T get() {
		final ListenableFutureTask<T> futureTask = ListenableFutureTask.create(new Callable<T>() {
			@Override
			public T call() {
				final T view = createView();
				view.setWindowFlags(WindowType.X11BypassWindowManagerHint);
				view.setAttribute(	WidgetAttribute.WA_DeleteOnClose,
									true);
				view.setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
									true);
				return view;
			}
		});
		QApplication.invokeLater(futureTask);
		try {
			return futureTask.get();
		} catch (final InterruptedException e) {
			throw new Error(e);
		} catch (final ExecutionException e) {
			throw new Error(e);
		}
	}

	protected abstract T createView();

}
