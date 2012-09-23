package org.trinity.render.swt.impl;

import org.eclipse.swt.widgets.Composite;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.display.api.DisplaySurfaceFactory;
import org.trinity.foundation.render.api.PaintableSurfaceNode;
import org.trinity.render.swt.api.SwtPaintContext;
import org.trinity.render.swt.api.SwtRenderEngine;

public class SwtPaintContextImpl implements SwtPaintContext {

	private final PaintableSurfaceNode paintableSurfaceNode;
	private Composite visual;
	private final SwtRenderEngine swtRenderEngine;
	private final DisplaySurfaceFactory displaySurfaceFactory;

	public SwtPaintContextImpl(	PaintableSurfaceNode paintableSurfaceNode,
								Composite visual,
								SwtRenderEngine swtRenderEngine,
								DisplaySurfaceFactory displaySurfaceFactory) {
		this.paintableSurfaceNode = paintableSurfaceNode;
		this.visual = visual;
		this.swtRenderEngine = swtRenderEngine;
		this.displaySurfaceFactory = displaySurfaceFactory;
	}

	@Override
	public Composite getVisual() {
		return visual;
	}

	@Override
	public void setVisual(Composite qWidget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void evictVisual() {

	}

	@Override
	public Composite queryVisual(PaintableSurfaceNode paintableSurfaceNode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DisplaySurface getDisplaySurface(Composite visual) {
		return displaySurfaceFactory.createDisplaySurface(new SwtDisplaySurfaceHandle(visual));
	}

	@Override
	public void syncVisualGeometryToSurfaceNode(Composite visual,
												PaintableSurfaceNode paintableSurfaceNode) {

		int x = paintableSurfaceNode.getX();
		int y = paintableSurfaceNode.getY();
		int width = paintableSurfaceNode.getWidth();
		int height = paintableSurfaceNode.getHeight();
		boolean visible = paintableSurfaceNode.isVisible();

		visual.setLocation(	x,
							y);
		visual.setSize(	width,
						height);
		visual.setVisible(visible);
	}
}