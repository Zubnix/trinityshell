package org.trinity.render.qtlnf.impl;

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.shell.widget.api.view.ShellLabelView;

import com.google.inject.Inject;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
public class ShellLabelViewImpl extends ShellWidgetViewImpl implements ShellLabelView {

	private final DisplaySurfaceFactory displaySurfaceFactory;
	private Painter painter;

	@Inject
	ShellLabelViewImpl(final DisplaySurfaceFactory displaySurfaceFactory) {
		super(displaySurfaceFactory);
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Future<DisplaySurface> create(final Painter painter) {
		this.painter = painter;
		return painter.instruct(new PaintInstruction<DisplaySurface, QJPaintContext>() {
			@Override
			public DisplaySurface call(	final PaintableRenderNode paintableRenderNode,
										final QJPaintContext paintContext) {
				final QWidget parentVisual = paintContext.queryVisual(paintableRenderNode
						.getParentPaintableRenderNode());
				final QWidget visual = new QLabel(parentVisual);

				paintContext.syncVisualGeometryToNode(	visual,
														paintableRenderNode);

				final DisplaySurfaceHandle visualDisplaySurfaceHandle = paintContext.getDisplaySurfaceHandle(visual);

				final DisplaySurface displaySurface = ShellLabelViewImpl.this.displaySurfaceFactory
						.createDisplaySurface(visualDisplaySurfaceHandle);
				return displaySurface;
			}
		});
	}

	@Override
	public Future<Void> update(final String text) {
		return this.painter.instruct(new PaintInstruction<Void, QJPaintContext>() {
			@Override
			public Void call(	final PaintableRenderNode paintableRenderNode,
								final QJPaintContext paintContext) {
				final QLabel visual = (QLabel) paintContext.getVisual();
				visual.setText(text);
				return null;
			}
		});
	}
}