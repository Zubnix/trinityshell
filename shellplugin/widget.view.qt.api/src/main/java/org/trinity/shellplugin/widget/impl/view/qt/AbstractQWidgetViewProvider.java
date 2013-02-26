package org.trinity.shellplugin.widget.impl.view.qt;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Provider;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QWidget;

public abstract class AbstractQWidgetViewProvider<T extends QWidget> implements Provider<T> {

	private class ViewCreationRoutine implements PaintRoutine<T, PaintContext> {

		@Override
		public T call(final PaintContext paintContext) throws ExecutionException {
			final T view = createView();
			view.setWindowFlags(WindowType.X11BypassWindowManagerHint);
			view.setAttribute(	WidgetAttribute.WA_DeleteOnClose,
								true);
			view.setAttribute(	WidgetAttribute.WA_DontCreateNativeAncestors,
								true);
			return view;
		}

	}

	private final ViewCreationRoutine viewCreationRoutine = new ViewCreationRoutine();

	private final PaintRenderer paintRenderer;

	protected AbstractQWidgetViewProvider(final PaintRenderer paintRenderer) {
		this.paintRenderer = paintRenderer;
	}

	@Override
	public T get() {
		final ListenableFuture<T> futureView = this.paintRenderer.invoke(	this,
																			this.viewCreationRoutine);
		try {
			return futureView.get();
		} catch (final InterruptedException e) {
			throw new Error(e);
		} catch (final ExecutionException e) {
			throw new Error(e);
		}
	}

	protected abstract T createView();

}
