package org.trinity.render.qt.api;

import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintableSurfaceNode;

import com.trolltech.qt.gui.QWidget;

public interface QJPaintContext extends PaintContext {
	QWidget getVisual();

	void setVisual(QWidget qWidget);

	void evictVisual();

	QWidget queryVisual(PaintableSurfaceNode paintableSurfaceNode);

	DisplaySurface getDisplaySurface(QWidget visual);

	void syncVisualGeometryToSurfaceNode(	QWidget visual,
									PaintableSurfaceNode paintableSurfaceNode);
}
