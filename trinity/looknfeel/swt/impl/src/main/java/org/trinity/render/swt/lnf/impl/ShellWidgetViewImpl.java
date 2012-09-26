package org.trinity.render.swt.lnf.impl;

import java.util.concurrent.Future;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.Visual;
import org.trinity.shell.api.widget.ShellWidgetView;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetViewImpl implements ShellWidgetView {

	private Painter painter;

	protected Painter getPainter() {
		return this.painter;
	}

	protected <R> Future<R> invokePaintInstruction(final PaintInstruction<R, SwtPaintContext> paintInstruction) {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not yet created!");
		}
		return painter.instruct(paintInstruction);
	}

	@Override
	public final Future<Void> createDisplaySurface(final Painter painter) {
		if (this.painter == null) {
			this.painter = painter;
		} else {
			throw new IllegalStateException("Display surface already created!");
		}

		return invokePaintInstruction(new PaintInstruction<Void, SwtPaintContext>() {
			@Override
			public Void call(final SwtPaintContext paintContext) {
				createDisplaySurfaceInstruction(paintContext);
				return null;
			}
		});
	}

	protected void createDisplaySurfaceInstruction(final SwtPaintContext paintContext) {
		final PaintableSurfaceNode parentSurfaceNode = paintContext.getPaintableSurfaceNode()
				.getParentPaintableSurface();
		Visual parentVisual = null;
		if (parentSurfaceNode != null) {
			parentVisual = paintContext.queryVisual(parentSurfaceNode);
		}
		final Visual visual = createVisual(parentVisual);

		paintContext.syncVisualGeometryToSurfaceNode(visual);
		paintContext.setVisual(visual);
	}

	protected Visual createVisual(final Composite parentVisual) {
		final Visual visual = new Visual(	parentVisual,
											SWT.NO_TRIM);
		return visual;
	}

	@Override
	public final Future<Void> destroy() {
		return invokePaintInstruction(new PaintInstruction<Void, SwtPaintContext>() {
			@Override
			public Void call(final SwtPaintContext paintContext) {
				destroyInstruction(paintContext);
				return null;
			}
		});
	}

	protected void destroyInstruction(final SwtPaintContext paintContext) {
		final Visual visual = paintContext.getVisual();
		visual.dispose();
		paintContext.evictVisual();
	}

	@Override
	public final Future<DisplaySurface> getDislaySurface() {
		final Painter painter = getPainter();
		if (this.painter == null) {
			throw new IllegalStateException("Display surface not created!");
		}
		return painter.instruct(new PaintInstruction<DisplaySurface, SwtPaintContext>() {
			@Override
			public DisplaySurface call(final SwtPaintContext paintContext) {
				return getDisplaySurfaceInstruction(paintContext);
			}
		});
	}

	protected DisplaySurface getDisplaySurfaceInstruction(final SwtPaintContext paintContext) {
		final Visual visual = paintContext.getVisual();
		final DisplaySurface displaySurface = paintContext.getDisplaySurface(visual);
		return displaySurface;
	}

}
