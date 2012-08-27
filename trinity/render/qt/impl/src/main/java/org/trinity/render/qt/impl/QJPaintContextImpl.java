package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

import com.trolltech.qt.gui.QWidget;

public class QJPaintContextImpl implements QJPaintContext {

	private final PaintableRenderNode paintableRenderNode;
	private final QWidget visual;
	private final QJRenderEngine qjRenderEngine;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public QJPaintContextImpl(	final PaintableRenderNode paintableRenderNode,
								final QWidget visual,
								final QJRenderEngine qjRenderEngineImpl,
								final DisplaySurfaceFactory displaySurfaceFactory) {
		this.paintableRenderNode = paintableRenderNode;
		this.visual = visual;
		this.qjRenderEngine = qjRenderEngineImpl;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public QWidget getVisual() {
		return this.visual;
	}

	@Override
	public void setVisual(final QWidget qWidget) {
		this.qjRenderEngine.putVisual(	(DisplayEventSource) this.paintableRenderNode,
										this.paintableRenderNode,
										qWidget);
	}

	@Override
	public QWidget queryVisual(final PaintableRenderNode paintableRenderNode) {
		return this.qjRenderEngine.getVisual(paintableRenderNode);
	}

	@Override
	public DisplaySurface getDisplaySurface(final QWidget visual) {
		final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(visual);
		final DisplaySurface displaySurface = this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);

		return displaySurface;
	}

	@Override
	public void syncVisualGeometryToNode(	final QWidget visual,
											final PaintableRenderNode paintableRenderNode) {
		final int x = paintableRenderNode.getX();
		final int y = paintableRenderNode.getY();
		final int width = paintableRenderNode.getWidth();
		final int height = paintableRenderNode.getHeight();

		final boolean visible = paintableRenderNode.isVisible();

		visual.setGeometry(	x,
							y,
							width,
							height);
		visual.setVisible(visible);
	}

	@Override
	public void evictVisual() {
		this.qjRenderEngine.removeVisual(this.paintableRenderNode);
	}
}
