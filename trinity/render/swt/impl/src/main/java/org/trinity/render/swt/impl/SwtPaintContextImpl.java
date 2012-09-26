package org.trinity.render.swt.impl;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.Visual;

public class SwtPaintContextImpl implements SwtPaintContext {

	private final PaintableSurfaceNode paintableSurfaceNode;
	private final Visual visual;
	private final SwtRenderEngineImpl renderEngine;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public SwtPaintContextImpl(	final PaintableSurfaceNode paintableSurfaceNode,
								final Visual visual,
								final SwtRenderEngineImpl renderEngine,
								final DisplaySurfaceFactory displaySurfaceFactory) {
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.visual = visual;
		this.renderEngine = renderEngine;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Visual getVisual() {
		return this.visual;
	}

	@Override
	public void setVisual(final Visual visual) {

		this.renderEngine.put(	this.paintableSurfaceNode,
								visual);
	}

	@Override
	public void evictVisual() {
		this.renderEngine.remove(this.paintableSurfaceNode);
	}

	@Override
	public Visual queryVisual(final PaintableSurfaceNode paintableSurfaceNode) {
		return this.renderEngine.getVisual(paintableSurfaceNode);
	}

	@Override
	public DisplaySurface getDisplaySurface(final Visual visual) {
		return this.displaySurfaceFactory.createDisplaySurface(new SwtDisplaySurfaceHandle(visual));
	}

	@Override
	public void syncVisualGeometryToSurfaceNode(final Visual visual) {

		final int x = this.paintableSurfaceNode.getX();
		final int y = this.paintableSurfaceNode.getY();
		final int width = this.paintableSurfaceNode.getWidth();
		final int height = this.paintableSurfaceNode.getHeight();
		final boolean visible = this.paintableSurfaceNode.isVisible();

		visual.setLocation(	x,
							y);
		visual.setSize(	width,
						height);
		visual.setVisible(visible);
	}

	@Override
	public PaintableSurfaceNode getPaintableSurfaceNode() {
		return this.paintableSurfaceNode;
	}
}