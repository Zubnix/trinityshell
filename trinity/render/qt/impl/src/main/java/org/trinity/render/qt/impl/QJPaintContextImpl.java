package org.trinity.render.qt.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.display.api.DisplaySurfaceHandle;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.qt.api.QJPaintContext;

import com.trolltech.qt.gui.QWidget;

public class QJPaintContextImpl implements QJPaintContext {

	private final PaintableSurfaceNode paintableSurfaceNode;
	private final QJRenderEngineImpl qjRenderEngine;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public QJPaintContextImpl(	final PaintableSurfaceNode paintableSurfaceNode,
								final QJRenderEngineImpl qjRenderEngineImpl,
								final DisplaySurfaceFactory displaySurfaceFactory) {
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.qjRenderEngine = qjRenderEngineImpl;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public void setVisual(final QWidget qWidget) {
		this.qjRenderEngine.putVisual(	(DisplayEventSource) this.paintableSurfaceNode,
										this.paintableSurfaceNode,
										qWidget);
	}

	@Override
	public QWidget getVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		return this.qjRenderEngine.getVisual(paintableSurfaceNode);
	}

	@Override
	public DisplaySurface getDisplaySurface(final QWidget visual) {
		final DisplaySurfaceHandle displaySurfaceHandle = new QJDisplaySurfaceHandle(visual);
		final DisplaySurface displaySurface = this.displaySurfaceFactory.createDisplaySurface(displaySurfaceHandle);

		return displaySurface;
	}

	@Override
	public void syncVisualGeometryToSurfaceNode(final QWidget visual) {
		final int x = paintableSurfaceNode.getX();
		final int y = paintableSurfaceNode.getY();
		final int width = paintableSurfaceNode.getWidth();
		final int height = paintableSurfaceNode.getHeight();

		final boolean visible = paintableSurfaceNode.isVisible();

		visual.setGeometry(	x,
							y,
							width,
							height);
		visual.setVisible(visible);
	}

	@Override
	public void disposeVisual() {
		this.qjRenderEngine.removeVisual(this.paintableSurfaceNode);
	}

	@Override
	public PaintableSurfaceNode getPaintableSurfaceNode() {
		return paintableSurfaceNode;
	}
}
