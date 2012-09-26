package org.trinity.render.swt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

public interface SwtPaintContext extends PaintContext {

	Visual getVisual();

	void setVisual(Visual visual);

	void evictVisual();

	Visual queryVisual(PaintableSurfaceNode paintableSurfaceNode);

	DisplaySurface getDisplaySurface(Visual visual);

	void syncVisualGeometryToSurfaceNode(Visual visual);
}
