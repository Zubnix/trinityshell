package org.trinity.render.swt.api;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface SwtPaintContext extends PaintContext {

	Composite getVisual();

	void setVisual(Composite qWidget);

	void evictVisual();

	Composite queryVisual(PaintableSurfaceNode paintableSurfaceNode);

	DisplaySurface getDisplaySurface(Composite visual);

	void syncVisualGeometryToSurfaceNode(	Composite visual,
											PaintableSurfaceNode paintableSurfaceNode);
}
