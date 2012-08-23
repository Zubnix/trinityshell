package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.google.inject.Inject;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellButtonViewImpl extends ShellWidgetViewImpl {

	private final DisplaySurfaceFactory displaySurfaceFactory;

	private Painter painter;

	@Inject
	ShellButtonViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		super(displaySurfaceFactory);
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
						final QWidget parent = paintContext
								.queryVisual(paintableRenderNode
										.getParentPaintableRenderNode());
						final QPushButton visual = new QPushButton(parent);

						final DisplaySurfaceHandle displaySurfaceHandle = paintContext
								.getDisplaySurfaceHandle(visual);
						final DisplaySurface visualDisplaySurface = ShellButtonViewImpl.this.displaySurfaceFactory
								.createDisplaySurface(displaySurfaceHandle);
						return visualDisplaySurface;
					}
				});
	}
}
