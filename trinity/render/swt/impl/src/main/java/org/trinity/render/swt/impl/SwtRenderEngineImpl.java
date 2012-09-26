package org.trinity.render.swt.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.eclipse.swt.widgets.Display;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.SwtRenderEngine;
import org.trinity.render.swt.api.Visual;

public class SwtRenderEngineImpl implements SwtRenderEngine {

	private DisplaySurfaceFactory displaySurfaceFactory;
	private final Map<PaintableSurfaceNode, Visual> visuals = new HashMap<PaintableSurfaceNode, Visual>();

	@Override
	public <R> Future<R> invoke(final PaintableSurfaceNode paintableSurfaceNode,
								final PaintInstruction<R, SwtPaintContext> paintInstruction) {

		final FutureTask<R> futureTask = new FutureTask<R>(new Callable<R>() {

			@Override
			public R call() throws Exception {
				final Visual visual = getVisual(paintableSurfaceNode);
				final SwtPaintContext qjPaintContext = new SwtPaintContextImpl(	paintableSurfaceNode,
																				visual,
																				SwtRenderEngineImpl.this,
																				SwtRenderEngineImpl.this.displaySurfaceFactory);
				final R result = paintInstruction.call(qjPaintContext);
				return result;
			}
		});

		Display.getDefault().asyncExec(futureTask);
		return null;
	}

	protected Visual getVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		return this.visuals.get(paintableSurfaceNode);
	}

	protected void put(	final PaintableSurfaceNode paintableSurfaceNode,
						final Visual visual) {
		this.visuals.put(	paintableSurfaceNode,
							visual);
	}

	protected void remove(final PaintableSurfaceNode paintableSurfaceNode) {
		this.visuals.remove(paintableSurfaceNode);
	}
}