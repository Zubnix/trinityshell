package org.trinity.shellplugin.widget.impl.view.qt;

import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRenderer;
import org.trinity.foundation.api.render.PaintRoutine;
import org.trinity.foundation.api.render.binding.view.View;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Provider;

public abstract class AbstractViewProvider implements Provider<View> {

	private class ViewCreationRoutine implements PaintRoutine<View, PaintContext> {

		@Override
		public View call(PaintContext paintContext) throws ExecutionException {
			return createView();
		}

	}

	private final ViewCreationRoutine viewCreationRoutine = new ViewCreationRoutine();

	private final PaintRenderer paintRenderer;

	AbstractViewProvider(PaintRenderer paintRenderer) {
		this.paintRenderer = paintRenderer;
	}

	@Override
	public View get() {
		ListenableFuture<View> futureView = this.paintRenderer.invoke(	this,
																		viewCreationRoutine);
		try {
			return futureView.get();
		} catch (InterruptedException e) {
			// TODO handle this better...
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			// TODO handle this better...
			throw new RuntimeException(e);
		}
	}

	abstract View createView();

}
