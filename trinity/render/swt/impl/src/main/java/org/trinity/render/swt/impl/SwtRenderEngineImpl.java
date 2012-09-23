package org.trinity.render.swt.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.SwtRenderEngine;

public class SwtRenderEngineImpl implements SwtRenderEngine {

	private DisplaySurfaceFactory displaySurfaceFactory;

	@Override
	public <R> Future<R> invoke(final PaintableSurfaceNode paintableSurfaceNode,
								final PaintInstruction<R, SwtPaintContext> paintInstruction) {

		final FutureTask<R> futureTask = new FutureTask<R>(new Callable<R>() {

			@Override
			public R call() throws Exception {
				final Composite visual = getVisual(paintableSurfaceNode);
				final SwtPaintContext qjPaintContext = new SwtPaintContextImpl(	paintableSurfaceNode,
																				visual,
																				SwtRenderEngineImpl.this,
																				SwtRenderEngineImpl.this.displaySurfaceFactory);
				final R result = paintInstruction.call(	paintableSurfaceNode,
														qjPaintContext);
				return result;
			}
		});

		Display.getDefault().asyncExec(futureTask);
		return null;
	}

	protected Composite getVisual(PaintableSurfaceNode paintableSurfaceNode) {
		// TODO Auto-generated method stub
		return null;
	}

}