package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellWidgetView;

import com.google.inject.Inject;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellWidgetViewImpl implements ShellWidgetView {

	private final DisplaySurfaceFactory displaySurfaceFactory;

	private Painter painter;

	@Inject
	ShellWidgetViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;

		return this.painter
				.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
					@Override
					public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
												final QJPaintContext paintContext) {

						final QWidget parentVisual = paintContext
								.queryVisual(paintableRenderNode
										.getParentPaintableRenderNode());
						final QWidget visual = new QWidget(parentVisual);

						final DisplaySurfaceHandle visualDisplaySurfaceHandle = paintContext
								.createDisplaySurfaceHandle(visual);

						final DisplaySurface displaySurface = ShellWidgetViewImpl.this.displaySurfaceFactory
								.createDisplaySurface(visualDisplaySurfaceHandle);
						return displaySurface;
					}
				});
	}

	@Override
	public Future<Void> destroy() {
		return this.painter
				.instruct(new PaintInstruction<Void, QJPaintContext>() {
					@Override
					public Void call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
						final QWidget visual = paintContext.getVisual();
						visual.close();
						return null;
					}
				});
	}
}